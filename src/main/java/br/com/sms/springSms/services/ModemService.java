package br.com.sms.springSms.services;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.smslib.AGateway;
import org.smslib.IInboundMessageNotification;
import org.smslib.IOutboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.Library;
import org.smslib.Message.MessageTypes;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.Service.ServiceStatus;
import org.smslib.helper.CommPortIdentifier;
import org.smslib.modem.SerialModemGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.sms.springSms.daos.SmsDao;
import br.com.sms.springSms.models.Sms;

@org.springframework.stereotype.Service
@Transactional
public class ModemService {
	
	@Autowired
	SmsDao smsDao;

	public List<CommPortIdentifier> listModems() {
		Enumeration<CommPortIdentifier> modems = org.smslib.helper.CommPortIdentifier.getPortIdentifiers();
		List<CommPortIdentifier> modemList = new ArrayList<CommPortIdentifier>();
		while (modems.hasMoreElements()) {
			CommPortIdentifier param = modems.nextElement();
			modemList.add(param);
		}
		return modemList;
	}

	public CommPortIdentifier getSpecificModemInfo(String port) {
		return org.smslib.helper.CommPortIdentifier.getPortIdentifier(port);
	}
	
	@Transactional
	public void start(String com) throws Exception {
		OutboundNotification outboundNotification = new OutboundNotification();
		SMSInNotification inboundNotification = new SMSInNotification();
		System.out.println("Example: Send message from a serial gsm modem.");
		System.out.println(Library.getLibraryDescription());
		System.out.println("Version: " + Library.getLibraryVersion());
		SerialModemGateway gateway = new SerialModemGateway("port."+com,com, 115200, "Huawei", "");
		gateway.setInbound(true);
		gateway.setOutbound(true);
		gateway.setSimPin("8486");
		// Below is for VIVO BRAZIL - be sure to set your own!
		gateway.setSmscNumber("+550101102010");
		Service.getInstance().setOutboundMessageNotification(outboundNotification);
		Service.getInstance().setInboundMessageNotification(inboundNotification);
		Service.getInstance().addGateway(gateway);
		Service.getInstance().startService();
		System.out.println();
		System.out.println("Modem Information:");
		System.out.println("  Manufacturer: " + gateway.getManufacturer());
		System.out.println("  Model: " + gateway.getModel());
		System.out.println("  Serial No: " + gateway.getSerialNo());
		System.out.println("  SIM IMSI: " + gateway.getImsi());
		System.out.println("  Signal Level: " + gateway.getSignalLevel() + " dBm");
		System.out.println("  Battery Level: " + gateway.getBatteryLevel() + "%");
		
	}
	
	public void stop() throws Exception {
		Service.getInstance().stopService();
	}
	
	public ServiceStatus status(){
		return Service.getInstance().getServiceStatus();
	}
	
	
	public void sendSms(String number,String text) throws Exception{
		OutboundMessage msg = new OutboundMessage(number, text);
        Service.getInstance().sendMessage(msg);
	}
	
	
	@org.springframework.stereotype.Service @Transactional
	public class SMSInNotification implements IInboundMessageNotification {
		public void process(AGateway gateway, MessageTypes msgType, InboundMessage msg) {
			switch (msgType) {
			case INBOUND:
				Sms sms = new Sms();
				sms.setDate(msg.getDate());
				sms.setNumber(msg.getOriginator());
				sms.setText(msg.getText());
				smsDao.save(sms);
				break;
			case STATUSREPORT:
				break;
			default:
				break;
			}
		}
	}

	public class OutboundNotification implements IOutboundMessageNotification {
		public void process(AGateway gateway, OutboundMessage msg) {
			System.out.println("Outbound handler called from Gateway: " + gateway.getGatewayId());
			System.out.println(msg);
		}
	}
}
