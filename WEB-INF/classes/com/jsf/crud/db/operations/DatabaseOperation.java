package com.jsf.crud.db.operations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
 
import javax.faces.context.FacesContext;
 
import com.jsf.crud.PersonBean;
import com.jsf.crud.AppointmentBean;

public class DatabaseOperation {

    public static Statement stmtObj;
    public static Connection connObj;
    public static ResultSet resultSetObj;
    public static PreparedStatement pstmt;

    public static Connection getConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String db_url ="jdbc:mysql://localhost:3306/covid_web?useTimezone=true&serverTimezone=UTC",
                    db_userName = "root",
                    db_password = "!Gatin1@strovava";
            connObj = DriverManager.getConnection(db_url,db_userName,db_password);
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        return connObj;
    }
	
	public static ArrayList getAppointmentsListFromDB() {
		ArrayList appointmentsList = new ArrayList();
		try {
				stmtObj = getConnection().createStatement();
				resultSetObj = stmtObj.executeQuery("select cs.*, pp.nome as paciente, pm.nome as medico from consulta cs inner join pessoa pp on pp.id = id_paciente inner join pessoa pm on pm.id = id_medico");
				while(resultSetObj.next()) {
					AppointmentBean apoObj = new AppointmentBean();
					apoObj.setId(resultSetObj.getInt("id"));
					apoObj.setIdPaciente(resultSetObj.getInt("id_paciente"));
					apoObj.setIdMedico(resultSetObj.getInt("id_medico"));
					apoObj.setNomePaciente(resultSetObj.getString("paciente"));
					apoObj.setNomeMedico(resultSetObj.getString("medico"));
					apoObj.setDataConsulta(resultSetObj.getDate("data_consulta"));
					appointmentsList.add(apoObj);
				}
				System.out.println("Total de Registros: " + appointmentsList.size());
				connObj.close();
			} catch(Exception sqlException) {
				sqlException.printStackTrace();
			}
		return appointmentsList;
	}
	

    public static ArrayList getPersonsListFromDB(String category) {
        ArrayList personsList = new ArrayList();
        try {
            stmtObj = getConnection().createStatement();
            resultSetObj = stmtObj.executeQuery("select * from pessoa where categoria = '" + category + "'");
            while(resultSetObj.next()) {
                PersonBean perObj = new PersonBean();
                perObj.setId(resultSetObj.getInt("id"));
                perObj.setName(resultSetObj.getString("nome"));
                perObj.setEmail(resultSetObj.getString("email"));
                perObj.setCategory(resultSetObj.getString("categoria"));
                perObj.setGender(resultSetObj.getString("genero"));
                perObj.setAddress(resultSetObj.getString("endereco"));
                personsList.add(perObj);
            }
            System.out.println("Total de Registros: " + personsList.size());
            connObj.close();
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        } 
        return personsList;
    }
 
    public static String savePersonDetailsInDB(PersonBean newPersonObj, String lister, String adder) {
        int saveResult = 0;
        String navigationResult = "";
        try {      
            pstmt = getConnection().prepareStatement("insert into pessoa (nome, email, categoria, genero, endereco) values (?, ?, ?, ?, ?)");         
            pstmt.setString(1, newPersonObj.getName());
            pstmt.setString(2, newPersonObj.getEmail());
            pstmt.setString(3, newPersonObj.getCategory());
            pstmt.setString(4, newPersonObj.getGender());
            pstmt.setString(5, newPersonObj.getAddress());
            saveResult = pstmt.executeUpdate();
            connObj.close();
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        if(saveResult != 0) {
            navigationResult = lister + ".xhtml?faces-redirect=true";
        } else {
            navigationResult = adder + ".xhtml?faces-redirect=true";
        }
        return navigationResult;
    }
	
	public static String saveAppointmentDetailsInDB(AppointmentBean newAppointmentObj) {
        int saveResult = 0;
        String navigationResult = "";
        try {      
            pstmt = getConnection().prepareStatement("insert into consulta (id_paciente, id_medico, data_consulta) values (?, ?, ?)");         
            pstmt.setInt(1, newAppointmentObj.getIdPaciente());
            pstmt.setInt(2, newAppointmentObj.getIdMedico());
			pstmt.setDate(3, new java.sql.Date(newAppointmentObj.getDataConsulta().getTime()));
            saveResult = pstmt.executeUpdate();
            connObj.close();
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        if(saveResult != 0) {
            navigationResult = "listarConsultas.xhtml?faces-redirect=true";
        } else {
            navigationResult = "criarConsulta.xhtml?faces-redirect=true";
        }
        return navigationResult;
    }
 
    public static String editPersonRecordInDB(int personId, String page) {
        PersonBean editRecord = null;
        System.out.println("editPersonRecordInDB() : Person Id: " + personId);
 
        /* Salvando os dados da pessoa na sessão atual */
        Map<String,Object> sessionMapObj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
 
        try {
            stmtObj = getConnection().createStatement();    
            resultSetObj = stmtObj.executeQuery("select * from pessoa where id = "+personId);
            if(resultSetObj != null) {
                resultSetObj.next();
                editRecord = new PersonBean();
                editRecord.setId(resultSetObj.getInt("id"));
                editRecord.setName(resultSetObj.getString("nome"));
                editRecord.setEmail(resultSetObj.getString("email"));
                editRecord.setGender(resultSetObj.getString("genero"));
                editRecord.setAddress(resultSetObj.getString("endereco"));
                editRecord.setCategory(resultSetObj.getString("categoria"));
            }
            sessionMapObj.put("editRecordObj", editRecord);
            connObj.close();
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        return "/" + page + ".xhtml?faces-redirect=true";
    }
	
	public static String editAppointmentRecordInDB(int appointmentId) {
        AppointmentBean editRecord = null;
        System.out.println("editAppointmentRecordInDB() : Appointment Id: " + appointmentId);
 
        /* Salvando os dados da consulta na sessão atual */
        Map<String,Object> sessionMapObj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
 
        try {
            stmtObj = getConnection().createStatement();    
            resultSetObj = stmtObj.executeQuery("select * from consulta where id = "+appointmentId);
            if(resultSetObj != null) {
                resultSetObj.next();
                editRecord = new AppointmentBean();
                editRecord.setId(resultSetObj.getInt("id"));
                editRecord.setIdPaciente(resultSetObj.getInt("id_paciente"));
                editRecord.setIdMedico(resultSetObj.getInt("id_medico"));
				editRecord.setDataConsulta(resultSetObj.getDate("data_consulta"));
            }
            sessionMapObj.put("editRecordObj", editRecord);
            connObj.close();
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        return "/editarConsulta.xhtml?faces-redirect=true";
    }

    public static String updatePersonDetailsInDB(PersonBean updatePersonObj, String page) {
        try {
            pstmt = getConnection().prepareStatement("update pessoa set nome=?, email=?, categoria=?, genero=?, endereco=? where id=?");
            pstmt.setString(1,updatePersonObj.getName());
            pstmt.setString(2,updatePersonObj.getEmail());
            pstmt.setString(3,updatePersonObj.getCategory());
            pstmt.setString(4,updatePersonObj.getGender());
            pstmt.setString(5,updatePersonObj.getAddress());
            pstmt.setInt(6,updatePersonObj.getId());
            pstmt.executeUpdate();
            connObj.close();
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        return "/" + page + ".xhtml?faces-redirect=true";
    }
	
	public static String updateAppointmentDetailsInDB(AppointmentBean updateAppointmentObj) {
        try {
            pstmt = getConnection().prepareStatement("update consulta set id_paciente=?, id_medico=?, data_consulta=? where id=?");
            pstmt.setInt(1,updateAppointmentObj.getIdPaciente());
            pstmt.setInt(2,updateAppointmentObj.getIdMedico());
            pstmt.setDate(3,new java.sql.Date(updateAppointmentObj.getDataConsulta().getTime()));
			pstmt.setInt(4,updateAppointmentObj.getId());
            pstmt.executeUpdate();
            connObj.close();
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        return "/editarConsulta.xhtml?faces-redirect=true";
    }

    public static String deletePersonRecordInDB(int personId, String page){
        System.out.println("deletePersonRecordInDB() : Person Id: " + personId);
        try {
            pstmt = getConnection().prepareStatement("delete from pessoa where id = "+personId);
            pstmt.executeUpdate();
            connObj.close();
        } catch(Exception sqlException){
            sqlException.printStackTrace();
        }
        return "/" + page + ".xhtml?faces-redirect=true";
    }
	
	public static String deleteAppointmentRecordInDB(int appointmentId){
        System.out.println("deleteAppointmentRecordInDB() : Appointment Id: " + appointmentId);
        try {
            pstmt = getConnection().prepareStatement("delete from consulta where id = "+appointmentId);
            pstmt.executeUpdate();
            connObj.close();
        } catch(Exception sqlException){
            sqlException.printStackTrace();
        }
        return "/listarConsultas.xhtml?faces-redirect=true";
    }
}