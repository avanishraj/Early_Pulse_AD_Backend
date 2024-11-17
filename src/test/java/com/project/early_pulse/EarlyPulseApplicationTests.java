package com.project.early_pulse;

import com.project.early_pulse.services.AdminServiceImpl;
import com.project.early_pulse.services.AppointmentServiceImpl;
import com.project.early_pulse.services.LabServiceImpl;
import com.project.early_pulse.services.ReportServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class EarlyPulseApplicationTests {

	@MockBean
	private AdminServiceImpl adminService;
	@MockBean
	private ReportServiceImpl reportService;
	@MockBean
	private LabServiceImpl labService;
	@MockBean
	private AppointmentServiceImpl appointmentService;
	@Test
	void contextLoads() {
	}

}
