package com.shakhawat.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name = "CONTACT_INFO")
public class Contact {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@NotEmpty(message = "*Please Provide Fullname Name")
	@NotNull
	@Size(min = 3, max = 100, message = "Name must be within 3 to 100 character long")
	@Column(nullable = false, name = "person_name", length = 100)
	private String personName;

	@Email(message = "Email should be valid")
	@NotEmpty(message = "*Please Provide Email Address")
	@NotNull
	@Column(nullable = false, name = "person_email", length = 100)
	private String personEmail;

	@Size(max = 30, message = "Mobile No. maximum 30 character long")
	@Column(nullable = true, name = "person_mobile", length = 30)
	private String personMobile;
	
	@Size(max = 400, message = "Subject maximum 400 character long")
	@Column(name = "contact_subject", length = 400, nullable = true)
	private String contactSubject;
	
	@NotEmpty(message = "*Please Provide Message")
	@NotNull
	@Size(max = 2000, message = "Message max. 2000 character long")
	@Column(name = "message", length = 2000)
	private String message;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = true)
	private Date createdAt;

}
