/*******************************************************************************
 * Copyright (c) 2017-2018 Aion foundation.
 *
 *     This file is part of the aion network project.
 *
 *     The aion network project is free software: you can redistribute it
 *     and/or modify it under the terms of the GNU General Public License
 *     as published by the Free Software Foundation, either version 3 of
 *     the License, or any later version.
 *
 *     The aion network project is distributed in the hope that it will
 *     be useful, but WITHOUT ANY WARRANTY; without even the implied
 *     warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *     See the GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with the aion network project source files.
 *     If not, see <https://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Aion foundation.
 *
 ******************************************************************************/
package org.aion.mcf.config;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Api configuration class.
 */
public final class CfgApi {

    public CfgApi() {
        this.rpc = new CfgApiRpc();
        this.zmq = new CfgApiZmq();
        this.filter = new CfgEvtFilter();
    }

    private CfgApiZmq zmq;

    private CfgApiRpc rpc;

    private CfgEvtFilter filter;

    public void fromXML(final XMLStreamReader sr) throws XMLStreamException {
        loop:
        while (sr.hasNext()) {
            int eventType = sr.next();
            switch (eventType) {
            case XMLStreamReader.START_ELEMENT:
                switch (sr.getLocalName()) {
                case "java":
                    this.zmq.fromXML(sr);
                    break;
                case "rpc":
                    this.rpc.fromXML(sr);
                    break;
                case "filter":
                    this.filter.fromXML(sr);
                    break;
                default:
                    Cfg.skipElement(sr);
                    break;
                }
                break;
            case XMLStreamReader.END_ELEMENT:
                break loop;
            }
        }
    }

    public String toXML() {
        final XMLOutputFactory output = XMLOutputFactory.newInstance();
        output.setProperty("escapeCharacters", false);
        XMLStreamWriter xmlWriter;
        String xml;
        try {
            Writer strWriter = new StringWriter();
            xmlWriter = output.createXMLStreamWriter(strWriter);
            xmlWriter.writeCharacters("\r\n\t");
            xmlWriter.writeStartElement("api");

            xmlWriter.writeCharacters(this.rpc.toXML());
            xmlWriter.writeCharacters(this.zmq.toXML());
            xmlWriter.writeCharacters(this.filter.toXML());

            xmlWriter.writeCharacters("\r\n\t");
            xmlWriter.writeEndElement();
            xml = strWriter.toString();
            strWriter.flush();
            strWriter.close();
            xmlWriter.flush();
            xmlWriter.close();
            return xml;
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
            return "";
        }
    }

    public CfgApiRpc getRpc() {
        return this.rpc;
    }

    public CfgApiZmq getZmq() {
        return this.zmq;
    }

    public CfgEvtFilter getFilter() {return this.filter; }

}
