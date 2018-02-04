package com.zolee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.zolee.domain.Person;
import com.zolee.repository.PersonRepository;
import com.zolee.service.PersonService;

@SpringUI
public class MainUI extends UI{

	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private PersonService personService;
	
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	private HorizontalLayout mainLayout;
	private VerticalLayout leftLayout;
	private HorizontalLayout inputLayout;
	private HorizontalLayout searchLayout;
	private HorizontalLayout resultLayout;
	private TextField nameInputField;
	private PasswordField passwordInputField;
	private TextField searchByNameField;
	private Label resultLabel;
	private Button saveButton;
	private Button searchButton;
	private Grid<Person> personListGrid;

	@Override
	protected void init(VaadinRequest request) {
		mainLayout = new HorizontalLayout();
		leftLayout = new VerticalLayout();
		inputLayout = new HorizontalLayout();
		searchLayout = new HorizontalLayout();
		resultLayout = new HorizontalLayout();
		nameInputField = new TextField("Ide írd a nevet:");
		passwordInputField = new PasswordField("Ide írd a jelszót:");
		searchByNameField = new TextField("Keresés név alapján");
		resultLabel = new Label("Még nincs eredmény");
		searchButton = new Button("Keresés");
		saveButton = new Button("Mentés");
		personListGrid = new Grid<>(Person.class);
		
		setGrid();
		
		mainLayout.addComponents(leftLayout, personListGrid);
		leftLayout.addComponents(inputLayout, saveButton, searchByNameField, searchLayout);
		inputLayout.addComponents(nameInputField, passwordInputField);
		searchLayout.addComponents(searchButton, resultLabel);
		
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		setContent(mainLayout);
		
		saveButton.addClickListener(e -> savePerson(new Person(nameInputField.getValue(), passwordInputField.getValue())));
		searchButton.addClickListener(e -> searchPersonByName(searchByNameField.getValue()));
	}

	private void setGrid() {
		List<Person> tempList = new ArrayList<Person>();
		tempList = personRepository.findAll();
		personListGrid.setItems(tempList);
	}
	
	private void savePerson(Person person) {
		personService.savePerson(person);
		setGrid();
	}
	
	private void searchPersonByName(String name) {
		Person tempPerson = personService.findByName(name);
		if(tempPerson!=null) {
			resultLabel.setValue(tempPerson.getPassword());
		}else {
			resultLabel.setValue("Nincs találat!");
		}
	}
}
