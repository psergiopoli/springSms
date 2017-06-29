package br.com.sms.springSms.controllers;

import java.util.List;

import org.smslib.helper.CommPortIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.sms.springSms.models.Message;
import br.com.sms.springSms.services.ModemService;

@RestController
@RequestMapping("/modem")
public class ModemController
{	
   
	@Autowired
	ModemService modemService;
   
	@RequestMapping(value = "list",method = RequestMethod.GET, produces = "application/json")
	public List<CommPortIdentifier> list() {
		return modemService.listModems();
	}

	@RequestMapping(value = "start/{com}",method = RequestMethod.GET, produces = "application/json")
	public Message start(@PathVariable("com") String porta) {
		try {
			modemService.start(porta);
		} catch (Exception e) {
			e.printStackTrace();
			return new Message("Deu algo errado com o modem amiguinho");
		}
		return new Message("Modem iniciado");
	}

	@RequestMapping(value = "stop",method = RequestMethod.GET, produces = "application/json")
	public Message stop() throws Exception {
		modemService.stop();
		return new Message("Modem parado");
	}
	
	@RequestMapping(value = "status",method = RequestMethod.GET, produces = "application/json")
	public Message status() {
		return new Message(modemService.status().name());
	}	
}
