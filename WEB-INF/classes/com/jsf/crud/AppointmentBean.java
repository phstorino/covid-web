package com.jsf.crud;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.jsf.crud.db.operations.DatabaseOperation;

@ManagedBean(name = "AppointmentBean") @RequestScoped
public class AppointmentBean {
	private int id;
	private int idPaciente;
	private int idMedico;
	private String nomePaciente;
	private String nomeMedico;
	private Date dataConsulta;
	
	public ArrayList appointmentsListFromDB;
	
	public Date getDataConsulta() {
		return this.dataConsulta;
	}
	
	public void setDataConsulta(Date dataConsulta) {
		this.dataConsulta = dataConsulta;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getIdPaciente() {
		return this.idPaciente;
	}
	
	public void setIdPaciente(int idPaciente) {
		this.idPaciente = idPaciente;
	}
	
	public int getIdMedico() {
		return this.idMedico;
	}
	
	public void setIdMedico(int idMedico) {
		this.idMedico = idMedico;
	}
	
	public String getNomePaciente() {
		return this.nomePaciente;
	}
	
	public void setNomePaciente(String nomePaciente) {
		this.nomePaciente = nomePaciente;
	}
	
	public String getNomeMedico() {
		return this.nomeMedico;
	}
	
	public void setNomeMedico(String nomeMedico) {
		this.nomeMedico = nomeMedico;
	}
	
	public ArrayList appointmentsList() {
		appointmentsListFromDB = DatabaseOperation.getAppointmentsListFromDB();
        return appointmentsListFromDB;
    }
	
	public String deleteAppointmentRecord(int appointmentId) {
        return DatabaseOperation.deleteAppointmentRecordInDB(appointmentId);
    }
	
	public String saveAppointmentDetails(AppointmentBean newAppointmentObj) {
		return DatabaseOperation.saveAppointmentDetailsInDB(newAppointmentObj);
    }
	
	public String editAppointmentRecord(int appointmentId) {
        return DatabaseOperation.editAppointmentRecordInDB(appointmentId);
    }
	
	public String updateAppointmentDetails(AppointmentBean updateAppointmentObj) {
        return DatabaseOperation.updateAppointmentDetailsInDB(updateAppointmentObj);
    }
}