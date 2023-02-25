package com.uit.DoAnTotNghiepBE;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
public class DoAnTotNghiepBeApplication {

	@Autowired
	private Environment env;

	@Autowired
	private DataSource dataSource;
	public static void main(String[] args) {
		SpringApplication.run(DoAnTotNghiepBeApplication.class, args);
	}

	// Thêm phương thức sau vào lớp MainApplication
	private void testConnection() {
		try (Connection connection = dataSource.getConnection()) {
			System.out.println("Kết nối cơ sở dữ liệu thành công!");
		} catch (SQLException e) {
			System.err.println("Không thể kết nối cơ sở dữ liệu: " + e.getMessage());
		}
	}

	// Thêm phương thức sau vào lớp MainApplication
	@PostConstruct
	private void init() {
		testConnection();
	}

}
