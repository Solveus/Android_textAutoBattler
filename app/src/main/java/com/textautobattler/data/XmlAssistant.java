/*
 * Copyright (c) 2021.
 *
 * Author: Finogenov Vasily
 * Nickname: Solveus, solveus_666
 *
 * Github: https://github.com/Solveus
 *
 *
 */

package com.textautobattler.data;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;


public class XmlAssistant {

    public final static String WEAPON_FILE_NAME = "gameWeapon.xml";
    public final static String GAME_CHARACTERS_FILE_NAME = "gameCharacters.xml";

    public enum fileType {character, weapon}

    ;

    private final static String WEAPON = "weapon";
    private final static String CHARACTER = "character";
    private final static String WEAPONS = "weapons";
    private final static String CHARACTERS = "characters";
    private final static String NAME = "name";
    private final static String QUALITY = "quality";
    private final static String TYPE = "type";
    private final static String PHYSIC = "physicDamage";
    private final static String MAGIC = "magicDamage";
    private final static String HEALTH = "health";

    private static String weaponPath;
    private static String characterPath;

    public static void initAssistant(Context context) {

        if (context == null)
            throw new NullPointerException();

        weaponPath = context.getExternalFilesDir(null) + "/" + WEAPON_FILE_NAME;
        characterPath = context.getExternalFilesDir(null) + "/" + GAME_CHARACTERS_FILE_NAME;
    }

    public static boolean fileWeaponExists() {

        File weaponFile = new File(weaponPath);
        return weaponFile.isFile();
    }

    public static boolean fileCharactersExists() {

        File charactersFile = new File(characterPath);
        return charactersFile.isFile();
    }

    /**
     * Parse all tags and return in how ArrayList<Weapon>
     */
    public static ArrayList<Weapon> parseWeapons() {

        ArrayList<Weapon> weaponList;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            File weaponFile = new File(weaponPath);
            Document document = builder.parse(weaponFile);
            NodeList weaponElements = document.getDocumentElement().getElementsByTagName(WEAPON);

            weaponList = new ArrayList<Weapon>();

            for (int i = 0; i < weaponElements.getLength(); i++) {
                Node weapon = weaponElements.item(i);
                NamedNodeMap attributes = weapon.getAttributes();
                weaponList.add(new Weapon(
                        attributes.getNamedItem(NAME).getNodeValue(),
                        Weapon.qualityWeapon.valueOf(attributes.getNamedItem(QUALITY).getNodeValue()),
                        Weapon.typeWeapon.valueOf(attributes.getNamedItem(TYPE).getNodeValue()),
                        Integer.parseInt(attributes.getNamedItem(PHYSIC).getNodeValue()),
                        Integer.parseInt(attributes.getNamedItem(MAGIC).getNodeValue()))
                );
            }
            return weaponList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Parse all tags and return in how ArrayList<GameCharacters>
     */
    public static ArrayList<GameCharacter> parseCharacters() {
        ArrayList<GameCharacter> characterList;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            File characterFile = new File(characterPath);
            Document document = builder.parse(characterFile);
            NodeList characterElements = document.getDocumentElement().getElementsByTagName(CHARACTER);

            characterList = new ArrayList<GameCharacter>();

            for (int i = 0; i < characterElements.getLength(); i++) {
                Node character = characterElements.item(i);
                NamedNodeMap attributes = character.getAttributes();
                characterList.add(new GameCharacter(
                        attributes.getNamedItem(NAME).getNodeValue(),
                        Integer.parseInt(attributes.getNamedItem(HEALTH).getNodeValue()),
                        Integer.parseInt(attributes.getNamedItem(PHYSIC).getNodeValue()),
                        Integer.parseInt(attributes.getNamedItem(MAGIC).getNodeValue()),
                        new Weapon())
                );
            }
            return characterList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Save weapon in XML file
     * as <Weapon name="..." quality="..." type="..." physicDamage="..." magicDamage="..."/>
     * where "..." Weapon class fields
     */
    public static void serializeWeapon(Weapon weapon) {

        // create if not exists
        if (!fileWeaponExists()) {
            createFile(fileType.weapon);
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();

            Document document = builder.parse(new File(weaponPath));
            Element root = document.getDocumentElement();
            Element newWeapon = document.createElement(WEAPON);

            newWeapon.setAttribute(NAME, weapon.getName());
            newWeapon.setAttribute(QUALITY, String.valueOf(weapon.getQuality()));
            newWeapon.setAttribute(TYPE, String.valueOf(weapon.getType()));
            newWeapon.setAttribute(PHYSIC, String.valueOf(weapon.getPhysicDamage()));
            newWeapon.setAttribute(MAGIC, String.valueOf(weapon.getMagicDamage()));

            root.appendChild(newWeapon);

            saveFile(weaponPath, document);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Save character in XML file
     * as <Character name="..." health="..." type="..." physicDamage="..." magicDamage="..."/>
     * where "..." GameCharacter class fields
     */
    public static void serializeCharacter(GameCharacter character) {

        // create if not exists
        if (!fileCharactersExists()) {
            createFile(fileType.character);
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();

            Document document = builder.parse(new File(characterPath));
            Element root = document.getDocumentElement();

            Element newCharacter = document.createElement(CHARACTER);
            newCharacter.setAttribute(NAME, character.getName());
            newCharacter.setAttribute(HEALTH, String.valueOf(character.getHealth()));
            newCharacter.setAttribute(PHYSIC, String.valueOf(character.getAttackPower()));
            newCharacter.setAttribute(MAGIC, String.valueOf(character.getMagicPower()));
            root.appendChild(newCharacter);

            saveFile(characterPath, document);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean removeGameCharacter(String name) {

        if (name == null)
            throw new NullPointerException();

        if (!fileCharactersExists()) {
            return false;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(characterPath));

            NodeList nodes = document.getElementsByTagName(CHARACTER);

            for (int i = 0; i < nodes.getLength(); i++) {
                Element character = (Element) nodes.item(i);

                if (character.getAttribute(NAME).equals(name)) {
                    character.getParentNode().removeChild(character);
                }
            }

            saveFile(characterPath, document);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean removeWeapon(String name) {

        if (name == null)
            throw new NullPointerException();

        if (!fileWeaponExists()) {
            return false;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(weaponPath));

            NodeList nodes = document.getElementsByTagName(WEAPON);

            for (int i = 0; i < nodes.getLength(); i++) {
                Element weapon = (Element) nodes.item(i);

                if (weapon.getAttribute(NAME).equals(name)) {
                    weapon.getParentNode().removeChild(weapon);
                }
            }

            saveFile(weaponPath, document);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private static void saveFile(String path, Document document) {
        try {
            DOMSource source = new DOMSource(document);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            StreamResult result = new StreamResult(path);
            transformer.transform(source, result);

            formatXML(document, transformer, source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Create file for save GameCharacters or Weapon objects
     * {@link fileType} uses for create some types files
     */
    private static void createFile(fileType type) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        String filePath;

        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element rootElement;

            if (type.ordinal() == 0) {
                rootElement = doc.createElement(CHARACTERS);
                filePath = characterPath;
            } else if (type.ordinal() == 1) {
                rootElement = doc.createElement(WEAPONS);
                filePath = weaponPath;
            } else {
                throw new IllegalArgumentException("Uncorrected file name!");
            }

            doc.appendChild(rootElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult file = new StreamResult(filePath);
            transformer.transform(source, file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // remove spaces from XML file
    private static void formatXML(Document document, Transformer transformer, DOMSource source, StreamResult result) {

        try {
            XPathFactory xfact = XPathFactory.newInstance();
            XPath xpath = xfact.newXPath();
            NodeList empty =
                    (NodeList) xpath.evaluate("//text()[normalize-space(.) = '']",
                            document, XPathConstants.NODESET);

            for (int i = 0; i < empty.getLength(); i++) {
                Node node = empty.item(i);
                node.getParentNode().removeChild(node);
            }
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
