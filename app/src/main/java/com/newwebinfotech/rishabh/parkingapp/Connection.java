package com.newwebinfotech.rishabh.parkingapp;

import java.io.InputStream;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;


public class Connection {
	public Integer Real(String name,String La,String LI) {
		System.out.println("Inside UpdEstimateSlip_method method");
		final String SOAP_ACTION = "http://tempuri.org/Locationtrack";
		final String METHOD_NAME = "Locationtrack";
		final String NAMESPACE = "http://tempuri.org/";
		final String SOAP_ADDRESS = "http://64.15.147.46/test/service.asmx";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("name", name);
		request.addProperty("La", La);
		request.addProperty("LI", LI);
		Integer Status = null;

		System.out.println("Soap object created");

		System.out.println("properties set at request     "
				+ request.toString());
		try {
			// Create envelope
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			System.out.println("envelope object created");
			// Set output SOAP object
			envelope.setOutputSoapObject(request);
			System.out.println("set output soap object");
			Marshal floatMarshal = new MarshalBase64();
			floatMarshal.register(envelope);
			AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(
					SOAP_ADDRESS);
			androidHttpTransport
					.setXmlVersionTag("<?xml version='1.0' encoding='utf-8'?>");
			androidHttpTransport.debug = true;
			SoapPrimitive response = null;
			System.out.println("soap address   " + SOAP_ADDRESS);

			// Invole web service
			androidHttpTransport.call(SOAP_ACTION, envelope);
			System.out.println("invoke web service  " + SOAP_ACTION);
			// Get the response

			response = (SoapPrimitive) envelope.getResponse();

			String res = response.toString();
			System.out.println("SOAP PRIMITIVE OBJEC CRETAED" + res);
				InputStream inputStream = null;
			String content = "";
			Status = 1;
		} catch (Exception e) {
			System.out.println("error is         " + e);
		}
		return Status;
	}
	
}
