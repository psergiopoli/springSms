package br.com.sms.springSms.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.sms.springSms.models.Message;
import br.com.sms.springSms.models.Sms;
import br.com.sms.springSms.services.ModemService;
import br.com.sms.springSms.services.SmsService;

@RestController
@RequestMapping("/sms")
public class SmsController
{	
	
	@Autowired
	SmsService smsService;
	
	@Autowired
	ModemService modemService;
   
	@RequestMapping(value = "list",method = RequestMethod.GET, produces = "application/json")
	public List<Sms> list() {
		return smsService.listSms();
	}
	
	@RequestMapping(value = "send/{number}/{text}",method = RequestMethod.GET, produces = "application/json")
	public Message send(@PathVariable("number") String number,@PathVariable("text") String text) {
		try {
			modemService.sendSms(number, text);
		} catch (Exception e) {
			e.printStackTrace();
			return new Message("Deu algo errado amiguinho");			
		}
		return new Message("Mensagem enviada com sucesso");
	}
	
}
