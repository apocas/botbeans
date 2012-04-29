/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.botbeans.shapes.utilities;

import java.util.Scanner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Apocas
 */
public class BotBeansUtilitiesTest {

    public BotBeansUtilitiesTest() {
    }

    @Test
    public void testConvertXMLCondition() throws Exception {
        //String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859\"?><CODEBLOCKS><Pages><Page page-name=\"Canvas\" page-color=\"40 40 40\" page-width=\"1920\" page-infullview=\"yes\" page-drawer=\"Canvas\" ><PageBlocks><Block id=\"85\" genus-name=\"variable\" ><Label>b</Label><Location><X>346</X><Y>185</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"number\" init-type=\"number\" label=\"\" position-type=\"mirror\" con-block-id=\"83\" ></BlockConnector></Plug></Block><Block id=\"81\" genus-name=\"number\" ><Label>5</Label><Location><X>425</X><Y>185</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"number\" init-type=\"number\" label=\"\" position-type=\"mirror\" con-block-id=\"83\" ></BlockConnector></Plug></Block><Block id=\"83\" genus-name=\"sum\" ><Location><X>336</X><Y>182</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"number\" init-type=\"number\" label=\"\" position-type=\"mirror\" con-block-id=\"77\" ></BlockConnector></Plug><Sockets num-sockets=\"2\" ><BlockConnector connector-kind=\"socket\" connector-type=\"number\" init-type=\"number\" label=\"\" position-type=\"bottom\" con-block-id=\"85\" ></BlockConnector><BlockConnector connector-kind=\"socket\" connector-type=\"number\" init-type=\"number\" label=\"\" position-type=\"bottom\" con-block-id=\"81\" ></BlockConnector></Sockets></Block><Block id=\"79\" genus-name=\"variable\" ><Label>a</Label><Location><X>257</X><Y>186</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"number\" init-type=\"number\" label=\"\" position-type=\"mirror\" con-block-id=\"77\" ></BlockConnector></Plug></Block><Block id=\"77\" genus-name=\"greaterthan\" ><Location><X>247</X><Y>179</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"boolean\" init-type=\"boolean\" label=\"\" position-type=\"mirror\" ></BlockConnector></Plug><Sockets num-sockets=\"2\" ><BlockConnector connector-kind=\"socket\" connector-type=\"number\" init-type=\"number\" label=\"\" position-type=\"bottom\" con-block-id=\"79\" ></BlockConnector><BlockConnector connector-kind=\"socket\" connector-type=\"number\" init-type=\"number\" label=\"\" position-type=\"bottom\" con-block-id=\"83\" ></BlockConnector></Sockets></Block></PageBlocks></Page></Pages></CODEBLOCKS>";
        String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859\"?><CODEBLOCKS><Pages><Page page-name=\"Canvas\" page-color=\"40 40 40\" page-width=\"1920\" page-infullview=\"yes\" page-drawer=\"Canvas\" ><PageBlocks><Block id=\"81\" genus-name=\"number\" ><Location><X>272</X><Y>171</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"number\" init-type=\"number\" label=\"\" position-type=\"mirror\" con-block-id=\"77\" ></BlockConnector></Plug></Block><Block id=\"79\" genus-name=\"variable\" ><Label>a</Label><Location><X>193</X><Y>171</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"number\" init-type=\"number\" label=\"\" position-type=\"mirror\" con-block-id=\"77\" ></BlockConnector></Plug></Block><Block id=\"77\" genus-name=\"lessthan\" ><Location><X>183</X><Y>168</Y></Location><Plug><BlockConnector connector-kind=\"plug\" connector-type=\"boolean\" init-type=\"boolean\" label=\"\" position-type=\"mirror\" ></BlockConnector></Plug><Sockets num-sockets=\"2\" ><BlockConnector connector-kind=\"socket\" connector-type=\"number\" init-type=\"number\" label=\"\" position-type=\"bottom\" con-block-id=\"79\" ></BlockConnector><BlockConnector connector-kind=\"socket\" connector-type=\"number\" init-type=\"number\" label=\"\" position-type=\"bottom\" con-block-id=\"81\" ></BlockConnector></Sockets></Block></PageBlocks></Page></Pages></CODEBLOCKS>";

        ShapesUtilities.convertXMLCondition(xml);

    }
}
