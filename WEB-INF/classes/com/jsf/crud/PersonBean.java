package com.jsf.crud;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.jsf.crud.db.operations.DatabaseOperation;

@ManagedBean(name = "PersonBean") @RequestScoped
public class PersonBean {

    private int id;
    private String name;
    private String email;
    private String category;
    private String gender;
    private String address;

    public ArrayList personsListFromDB;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

	/*
    @PostConstruct
    public void init() {
        personsListFromDB = DatabaseOperation.getPersonsListFromDB();
    }
	*/

    public ArrayList personsList(String category) {
		personsListFromDB = DatabaseOperation.getPersonsListFromDB(category);
        return personsListFromDB;
    }

    public String savePersonDetails(PersonBean newPersonObj, String lister, String adder, int category) {
		if(category == 0) {
			newPersonObj.category = "0";
		}
		else {
			newPersonObj.category = "1";
		}
        return DatabaseOperation.savePersonDetailsInDB(newPersonObj, lister, adder);
    }

    public String editPersonRecord(int personId, String page) {
        return DatabaseOperation.editPersonRecordInDB(personId, page);
    }

    public String updatePersonDetails(PersonBean updatePersonObj, String page) {
        return DatabaseOperation.updatePersonDetailsInDB(updatePersonObj, page);
    }

    public String deletePersonRecord(int personId, String page) {
        return DatabaseOperation.deletePersonRecordInDB(personId, page);
    }
}