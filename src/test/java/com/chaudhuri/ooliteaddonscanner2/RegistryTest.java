/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.PlistParser;
import com.chaudhuri.ooliteaddonscanner2.model.Equipment;
import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.ExpansionManifest;
import com.chaudhuri.ooliteaddonscanner2.model.Ship;
import com.chaudhuri.ooliteaddonscanner2.model.Wikiworthy;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author hiran
 */
public class RegistryTest {
    private static final Logger log = LogManager.getLogger();
    
    public RegistryTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of addExpansions method, of class Registry.
     */
    @Test
    public void testAddExpansions() {
        System.out.println("addExpansions");
    }

    /**
     * Test of addExpansion method, of class Registry.
     */
    @Test
    public void testAddExpansion_PlistParserDictionaryContext() {
        System.out.println("addExpansion");
    }

    /**
     * Test of addExpansion method, of class Registry.
     */
    @Test
    public void testAddExpansion_Expansion() {
        log.info("addExpansion");
        
        Registry registry = new Registry();
        assertNotNull(registry.getExpansions());
        assertEquals(0, registry.getExpansions().size());
        
        Expansion expansion = new Expansion();
        try {
            registry.addExpansion(expansion);
            fail("Expected exception but caught none.");
        } catch (IllegalArgumentException e) {
            log.debug("caught expected exception", e);
        }
        
        expansion.setIdentifier("blah");
        registry.addExpansion(expansion);
        assertNotNull(registry.getExpansions());
        assertEquals(1, registry.getExpansions().size());
    }

    /**
     * Test of addEquipmentList method, of class Registry.
     */
    @Test
    public void testAddEquipmentList_Expansion_List() throws Exception {
        System.out.println("addEquipmentList");
    }

    /**
     * Test of addEquipmentList method, of class Registry.
     */
    @Test
    public void testAddEquipmentList_Expansion_PlistParserListContext() throws Exception {
        System.out.println("addEquipmentList");
    }

    /**
     * Test of addEquipment method, of class Registry.
     */
    @Test
    public void testAddEquipment_Expansion_List() throws Exception {
        System.out.println("addEquipment");
    }

    /**
     * Test of addShip method, of class Registry.
     */
    @Test
    public void testAddShip_Ship() {
        log.info("addShip");
        
        Registry registry = new Registry();
        log.debug("ships in registry: {}", registry.getShips());
        assertEquals(0, registry.getShips().size());
        assertEquals(0, registry.getWarnings().size());
        
        try {
            registry.addShip(new Ship());
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            log.debug("caught expected exception", e);
        }

        Expansion oxp = new Expansion();
        
        Ship ship = new Ship("s1");
        ship.setExpansion(oxp);
        registry.addShip(ship);
        log.debug("ships in registry: {}", registry.getShips());
        assertEquals(1, registry.getShips().size());
        assertEquals(0, registry.getWarnings().size());
        
        Ship ship2 = new Ship("s2");
        ship2.setExpansion(oxp);
        registry.addShip(ship2);
        log.debug("ships in registry: {}", registry.getShips());
        assertEquals(2, registry.getShips().size());
        assertEquals(0, registry.getWarnings().size());
        
        Ship ship3 = new Ship("s2");
        ship3.setExpansion(oxp);
        registry.addShip(ship3);
        log.debug("ships in registry: {}", registry.getShips());
        assertEquals(2, registry.getShips().size());
        assertEquals(1, registry.getWarnings().size());
    }

    /**
     * Test of addEquipment method, of class Registry.
     */
    @Test
    public void testAddEquipment_Equipment() {
        System.out.println("addEquipment");
    }

    /**
     * Test of addEquipment method, of class Registry.
     */
    @Test
    public void testAddEquipment_Expansion_PlistParserListContext() throws Exception {
        System.out.println("addEquipment");
    }

    /**
     * Test of addShipList method, of class Registry.
     */
    @Test
    public void testAddShipList_Expansion_Map() {
        System.out.println("addShipList");
    }

    /**
     * Test of addShipList method, of class Registry.
     */
    @Test
    public void testAddShipList_Expansion_PlistParserDictionaryContext() {
        System.out.println("addShipList");
    }

    /**
     * Test of addShip method, of class Registry.
     */
    @Test
    public void testAddShip_3args_1() {
        System.out.println("addShip");
    }

    /**
     * Test of addShip method, of class Registry.
     */
    @Test
    public void testAddShip_3args_2() {
        System.out.println("addShip");
    }

    /**
     * Test of getExpansionsByName method, of class Registry.
     */
    @Test
    public void testGetExpansionsByName() {
        log.info("getExpansionsByName");
        
        Registry registry = new Registry();
        Expansion e1 = new Expansion();
        e1.setIdentifier("C");
        e1.setTitle("A");
        Expansion e2 = new Expansion();
        e2.setIdentifier("B");
        e2.setTitle("B");
        Expansion e3 = new Expansion();
        e3.setIdentifier("A");
        e3.setTitle("C");
        registry.addExpansion(e1);
        registry.addExpansion(e2);
        registry.addExpansion(e3);
        
        List<Expansion> list = null;
        list = registry.getExpansions();
        log.debug("expansions: {}", list);
        assertEquals(e3, list.get(0));
        assertEquals(e2, list.get(1));
        assertEquals(e1, list.get(2));

        list = registry.getExpansionsByName();
        log.debug("expansions: {}", list);
        assertEquals(e1, list.get(0));
        assertEquals(e2, list.get(1));
        assertEquals(e3, list.get(2));
    }

    /**
     * Test of getEquipment method, of class Registry.
     */
    @Test
    public void testGetEquipment() {
        System.out.println("getEquipment");
    }

    /**
     * Test of getEquipmentByName method, of class Registry.
     */
    @Test
    public void testGetEquipmentByName() {
        System.out.println("getEquipmentByName");
    }

    /**
     * Test of getShips method, of class Registry.
     */
    @Test
    public void testGetShips() {
        System.out.println("getShips");
    }

    /**
     * Test of getShipsByName method, of class Registry.
     */
    @Test
    public void testGetShipsByName() {
        System.out.println("getShipsByName");
    }

    /**
     * Test of getAllByIdentifier method, of class Registry.
     */
    @Test
    public void testGetAllByIdentifier() {
        System.out.println("getAllByIdentifier");
    }

    /**
     * Test of toManifest method, of class Registry.
     */
    @Test
    public void testToManifest_Map() {
        System.out.println("toManifest");
    }

    /**
     * Test of toManifest method, of class Registry.
     */
    @Test
    public void testToManifest_PlistParserDictionaryContext() {
        System.out.println("toManifest");
    }

    /**
     * Test of addWarning method, of class Registry.
     */
    @Test
    public void testAddGetWarning() {
        log.info("addGetWarning");
        
        Registry registry = new Registry();
        assertNotNull(registry.getWarnings());
        assertEquals(0, registry.getWarnings().size());
        
        registry.addWarning("mywarning");
        assertEquals(1, registry.getWarnings().size());
        assertEquals("mywarning", registry.getWarnings().get(0));
        
        Expansion expansion = new Expansion();
        expansion.setIdentifier("myoxp");
        registry.addExpansion(expansion);
        assertEquals(1, registry.getWarnings().size());
        assertEquals("mywarning", registry.getWarnings().get(0));

        expansion.addWarning("expansion warning");
        assertEquals(2, registry.getWarnings().size());
        assertEquals("expansion warning", registry.getWarnings().get(1));

        assertEquals(1, registry.getGlobalWarnings().size());
        assertEquals("mywarning", registry.getGlobalWarnings().get(0));
    }

    /**
     * Test of getGlobalWarnings method, of class Registry.
     */
    @Test
    public void testGetGlobalWarnings() {
        System.out.println("getGlobalWarnings");
    }

    /**
     * Test of getProperty method, of class Registry.
     */
    @Test
    public void testGetProperties() {
        log.info("testGetProperties");
        
        Registry registry = new Registry();
        assertNotNull(registry.getProperties());
        assertEquals(0, registry.getProperties().size());
    }
    
    @Test
    public void testSetGetProperty() {
        log.info("testSetGetProperty");

        Registry registry = new Registry();
        assertEquals(0, registry.getProperties().size());
        assertNull(registry.getProperty("mykey"));

        registry.setProperty("mykey", "myvalue");
        assertEquals(1, registry.getProperties().size());
        assertEquals("myvalue", registry.getProperty("mykey"));
        
        registry.setProperty("mykey", null);
        assertNull(registry.getProperty("mykey"));

        try {
            registry.setProperty(null, "myvalue");
            fail("expected exception but caught none");
        } catch (NullPointerException e) {
            log.debug("caught expected exception", e);
        }
    }
    
    @Test
    public void testOverwriteExpansion() {
        log.info("testOverwriteExpansion");
        
        Registry registry = new Registry();
        assertEquals(0, registry.getExpansions().size());
        assertEquals(0, registry.getWarnings().size());
        
        Expansion e1 = new Expansion("A");
        registry.addExpansion(e1);
        assertEquals(1, registry.getExpansions().size());
        assertEquals(0, registry.getWarnings().size());
        
        Expansion e2 = new Expansion("A");
        registry.addExpansion(e2);
        assertEquals(1, registry.getExpansions().size());
        assertEquals(1, registry.getWarnings().size());
        assertEquals("OXP Overwrite! A (null) and A (null) share same id A", registry.getWarnings().get(0));
    }
}
