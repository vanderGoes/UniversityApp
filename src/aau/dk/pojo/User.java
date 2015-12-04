package aau.dk.pojo;

public class User {
	public User(String name, String lastname, char role, int age){
		this.age=age;
		this.lastName= lastname;
		this.name= name;
		this.role=role;
	}
	
	private String name;
	private String lastName;
	private char role; // Visitor, professor, or student
	private int age;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public char getRole() {
		return role;
	}
	public void setRole(char role) {
		this.role = role;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	
}
