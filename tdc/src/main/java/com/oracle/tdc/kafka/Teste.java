package com.oracle.tdc.kafka;

import java.sql.SQLException;

import com.oracle.tdc.msg.Customer;

public class Teste {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Customer c = new Customer();
		
		c.setName("Silvio Cristiano");
		c.setAge(19);
		c.setCpf("0998868686");
		c.setStatus("Aprovado");
		
		AtpConnection r = new AtpConnection();
		r.insert(c);
	

	}

}
