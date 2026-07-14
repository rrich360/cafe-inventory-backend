package com.backend.cafe.serviceImpl;

import com.backend.cafe.config.JwtFilter;
import com.backend.cafe.constants.CafeConstants;
import com.backend.cafe.dao.StockOrdersDao;
import com.backend.cafe.dao.VendorsDao;
import com.backend.cafe.model.StockOrders;
import com.backend.cafe.model.Vendors;
import com.backend.cafe.service.StockOrdersService;
import com.backend.cafe.utils.CafeUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class StockOrdersServiceImpl implements StockOrdersService {

    @Autowired
    StockOrdersDao stockOrdersDao;

    @Autowired
    VendorsDao vendorsDao;

    @Autowired
    JwtFilter jwtFilter;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy_HH:mm");
    LocalDate today = LocalDate.now();

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        log.info("Inside the generateReport() method");
        String timeStamp = LocalDateTime.now().format(formatter);
        LocalDate estArrivalOfShipment = today.plusWeeks(2); //set a default for estimate of arrival time for stock shipment.
        try {
             new File(CafeConstants.STOCK_ORDER_LOCATION).mkdirs();
             String fileName;
             if (validateRequestMap(requestMap)){
                 if (requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate")) {
                     fileName = (String) requestMap.get("uuid"); //extracts the stock order ID and passes it to the fileName String obj.
            } else {
                fileName = CafeUtils.getUUID();
                requestMap.put("uuid", fileName);
                insertStockOrder(requestMap); //passes data from the request map to the table in the database
            }
            String data = "\n" + "Created Date: " + timeStamp + "\n" + "Company Name: " + requestMap.get("companyName") + "\n" + "Contact Number: " + requestMap.get("contactNumber") +
                    "\n"+"Email: " + requestMap.get("email") + "\n" + "Payment Method: " + requestMap.get("paymentMethod") + "\n";
                    //+ "S.O. created by User: " + requestMap.getOrDefault("purchaseOrderOrganizer", jwtFilter.getCurrentUser()) + "\n" ;

            String data2 = "Vendor Name: " +
                    requestMap.get("vendorName") + "\n"
                    + "Email: " + requestMap.get("vendorEmail") + "\n" +
                    "Billing Address: " + requestMap.get("billingAddress") + "\n";

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(CafeConstants.STOCK_ORDER_LOCATION + "\\" + fileName + ".pdf"));
            document.open();
            setRectangleInPdf(document);

            Paragraph chunk = new Paragraph("Cafe Inventory Stock Order", getFont("Header"));
            chunk.setAlignment(Element.ALIGN_CENTER);
            document.add(chunk);
            Paragraph paragraph = new Paragraph(data + "\n \n", getFont("Data"));
            document.add(paragraph);
            Paragraph paragraph2 = new Paragraph(data2 + "\n \n", getFont("Data"));
            document.add(paragraph2);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            addTableHeader(table);
            //create method in CafeUtils to convert type String into Json format.
            JSONArray jsonArray = CafeUtils.getJsonArrayFromString((String) requestMap.get("orderDetails"));
            for (int i = 0; i < jsonArray.length(); i++) {//each iteration will extract 1 JSON object and add it into the table row.
                addRows(table, CafeUtils.getMapFromJson(jsonArray.getString(i)));
            }
            document.add(table); //table is added to pdf

            Paragraph footer = new Paragraph("\n" + "Total : " + requestMap.get("totalValue") + "\n" +
                    "Estimated time of shipment arrival : " + estArrivalOfShipment + "\n" +
                    "Thank you for choosing Cafe Inventory Management App!", getFont("Data"));

            document.add(footer);
            document.close();

            return new ResponseEntity<>("{\"uuid\":\""+fileName+"\"}", HttpStatus.OK);
        }
        return CafeUtils.getResponseEntity("Required data not found.", HttpStatus.BAD_REQUEST);
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addRows(PdfPTable table, Map<String, Object> data) {
        log.info("Inside addRows");
        table.addCell((String) data.get("stock item"));
        table.addCell((String) data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString(((Number) data.get("price")).doubleValue()));
        table.addCell(Double.toString(((Number) data.get("total")).doubleValue()));
    }

    private void addTableHeader(PdfPTable table) {
        log.info("Inside addTableHeader");
        Stream.of("Stock Item(s)", "Category", "Quantity", "Price", "Sub Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(BaseColor.GREEN);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });

        }

    private Font getFont(String type) {
        log.info("Inside getFont");
        switch (type) {
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,11,BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();
        }
    }

    private void setRectangleInPdf(Document document) throws DocumentException {//grid format for how the inventory items and price will display...
        log.info("Inside the setRectangleInPdf() method.");
        Rectangle rect = new Rectangle(577, 825, 18, 15);
        rect.enableBorderSide(1);//*2
        rect.enableBorderSide(2);
        rect.enableBorderSide(4);
        rect.enableBorderSide(8);
        rect.setBorderColor(BaseColor.BLACK);
        rect.setBorderWidth(1);
        document.add(rect);
    }

    private void insertStockOrder(Map<String, Object> requestMap) {
        try {
            Vendors vendor = new Vendors();
            vendor.setVendorName((String) requestMap.get("vendorName"));
            vendor.setVendorEmail((String) requestMap.get("vendorEmail"));
            vendor.setBillingAddress((String) requestMap.get("billingAddress"));
            vendorsDao.save(vendor);

            StockOrders stockOrders = new StockOrders();
            stockOrders.setUuid((String)requestMap.get("uuid"));
            stockOrders.setCompanyName((String)requestMap.get("companyName"));
            stockOrders.setEmail((String)(requestMap.get("email")));
            stockOrders.setContactNumber((String)requestMap.get("contactNumber"));
            stockOrders.setPaymentMethod((String) requestMap.get("paymentMethod"));
            //FIXED - .toString() works whether totalValue arrives as Double or String
            stockOrders.setTotalValue(Double.parseDouble(requestMap.get("totalValue").toString()));
            stockOrders.setOrderDetails((String) requestMap.get("orderDetails"));
            stockOrders.setPurchaseOrderOrganizer(jwtFilter.getCurrentUser());
            stockOrdersDao.save(stockOrders);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String, Object> requestMap){//method that contains keys from Map in which you pass the data into the API.
        return requestMap.containsKey("companyName") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("paymentMethod") &&
                requestMap.containsKey("orderDetails") &&
                requestMap.containsKey("totalValue");
    }

    @Override
    public ResponseEntity<List<StockOrders>> getStockOrders() {
        List<StockOrders> stockOrdersList = new ArrayList<>();
        if (jwtFilter.isAdmin()){
            stockOrdersList = stockOrdersDao.getAllStockOrders();
        }else {
            stockOrdersList = stockOrdersDao.getStockOrdersByUserName(jwtFilter.getCurrentUser());
        }
        return new ResponseEntity<>(stockOrdersList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        log.info("Inside getPdf : requestMap {}", requestMap);
        try {
            byte[] byteArray = new byte[0];
            //FIXED
            if(!requestMap.containsKey("uuid"))
                return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
            String filePath = CafeConstants.STOCK_ORDER_LOCATION + "\\" + (String) requestMap.get("uuid") + ".pdf";
            if (CafeUtils.isFileExist(filePath)){
                byteArray = getByteArray(filePath);//convert file path to byte array
                return new ResponseEntity<>(byteArray, HttpStatus.OK);// afterward we need to return the byte array to the API
            } else {
                requestMap.put("isGenerate", false);
                generateReport(requestMap); //generates Pdf and stores it to the designated location if it does not find the Pdf
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new byte[0], HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private byte[] getByteArray(String filePath) throws Exception {
        File initialFile = new File(filePath);
        InputStream targetStream = new FileInputStream(initialFile);
        byte[] byteArray = IOUtils.toByteArray(targetStream);
        targetStream.close();
        return byteArray;
    }

    @Override
    public ResponseEntity<String> deleteStockOrder(Integer id) {
        try {
            Optional<StockOrders> optional = stockOrdersDao.findById(id);
            if (optional.isPresent()){
                stockOrdersDao.deleteById(id);
                return CafeUtils.getResponseEntity("Stock Order deleted successfully", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity("Stock Order ID does not exist", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
