/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mongodb.morphia.jodatimeconverter;

import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author CZC
 */
public class JodaDateTimeConverterNGTest {

    public JodaDateTimeConverterNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void shouldEncode() {
        // GIVEN
        DateTime dateTime = DateTime.now();
        JodaDateTimeConverter converter = new JodaDateTimeConverter();

        // WHEN
        Object encodedDateTime = converter.encode(dateTime);

        // THEN
        assertEquals(encodedDateTime, dateTime.toDate());
    }

    @Test
    public void shouldDecode() {
        // GIVEN
        DateTime dateTime = DateTime.now();
        JodaDateTimeConverter converter = new JodaDateTimeConverter();

        // WHEN
        Object encodedDateTime = converter.encode(dateTime);
        Object decodedDateTime = converter.decode(DateTime.class, encodedDateTime);

        // THEN
        assertEquals(decodedDateTime, new DateTime(dateTime.getMillis()));
    }

    @Test
    public void shouldSave() throws UnknownHostException {
        // GIVEN
        Morphia morphia = new Morphia();
        MongoClient mongoClient = new MongoClient();

        String dbName = "test-jodadatetimeconverter";
        Datastore datastore = morphia.createDatastore(mongoClient, dbName);
        datastore.getDB().dropDatabase();

        TestDBEntity entity = new TestDBEntity();
        DateTime dateTime = DateTime.now().withZone(DateTimeZone.UTC);
        entity.setDateTime(dateTime);

        // WHEN
        datastore.save(entity);

        TestDBEntity entityFromDB = datastore.get(entity);

        // THEN
        // Note. DateTimeZone is not currently stored.
        assertEquals(entityFromDB.getDateTime().getMillis(), entity.getDateTime().getMillis());

        // CLEAN
        datastore.getDB().dropDatabase();
        mongoClient.close();
    }

}
