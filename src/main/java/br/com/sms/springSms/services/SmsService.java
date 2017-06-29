package br.com.sms.springSms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.sms.springSms.daos.SmsDao;
import br.com.sms.springSms.models.Sms;

@org.springframework.stereotype.Service
@Transactional
public class SmsService {
	
	@Autowired
	SmsDao smsDao;
	
	@Autowired
	ModemService modemService;

	public List<Sms> listSms() {
		return smsDao.all();
	}

}
