/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import org.junit.Test;
import static org.junit.Assert.*;
import cucumber.api.junit.Cucumber;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

/**
 *
 * @author alberto
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "test/resources")
public class CukesRunnerTest {
    
    public CukesRunnerTest() {
    }
    
}
