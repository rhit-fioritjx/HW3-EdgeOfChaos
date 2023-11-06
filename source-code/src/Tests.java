import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Tests {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		//EdgeOfChaosAutomata chaos = new EdgeOfChaosAutomata(5, 4, 0, new Random(5555));
		
		//chaos.step();
		
		
	}
	
	@Test
	void RandomTable() {
		
		double min_lambda =     0.05;
		double max_lambda = 1 - 0.05;
		int num_steps = 100;
		double step_size = (max_lambda - min_lambda) / num_steps;
		
		EdgeOfChaosAutomata chaos = new EdgeOfChaosAutomata(5, 4, min_lambda, new Random(5555));
		
		
		
		for(int i=0;i<num_steps;i++) {
			
			
			
			chaos.initializeState(128, 5, .5);
			
			int transient_length = 0;
			
			do{
				transient_length++;
			}while(!chaos.step());
			
			System.out.println(chaos.getLambda() + ": " + transient_length);
			
			chaos.increaseLambda(step_size);
			
		}
		
	}
	

}
