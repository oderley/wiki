package wiki;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.TakesScreenshot;

@RunWith(Parameterized.class)
public class Busca {
	//Atributos
	String url;
	WebDriver driver;
	static String pastaFoto = new SimpleDateFormat("yyyy-MM-dd HH-mm").format(Calendar.getInstance().getTime());
	
	// Métodos de Apoio
//	teste git 
	
	
	// Método para tirar print (screenshot)
	public void print (String nomeFoto) throws IOException {
		File foto = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(foto, new File("C:\\Iterasys\\FTS126-workspace\\wiki\\target\\"+ pastaFoto + "\\" + nomeFoto +".png"));
	}
		
	//Método para ler um arquivo CSV - Comma Separeted Values
	
	//Atributo da Massa de Tete
		String Id;
		String termo;
		String resultado;
		String tipo;
		String browser;
			
	//construtor para leitura dos campos (clicar com o botão direito, depois em Source e depois em Generate Constructor using fields) para gerar o codigo abaixo
	public Busca(String id, String termo, String resultado, String tipo, String browser) {
			this.Id = id;
			this.termo = termo;
			this.resultado = resultado;
			this.tipo = tipo;
			this.browser = browser;
		}
	
	// Coleção que informa o local e o nome do arquivo da massa
	@Parameters
	public static Collection <String[]> lerArquivo() throws IOException {
		//Chamar a coleção lerCSV e passar o caminho e o nome da massa
		return LerCSV("C:\\Iterasys\\FTS126-workspace\\wiki\\db\\massaWiki.csv");
	}
	
	public static Collection <String[]> LerCSV(String nomeMassa) throws IOException {
		//Realmente le o arquivo massaWiki.csv
		String linha;
		List<String[]> dados = new ArrayList<String[]>();
		BufferedReader arquivo = new BufferedReader(new FileReader(nomeMassa));
		while ((linha = arquivo.readLine()) != null) {
			String campos[] = linha.split(";");
			dados.add(campos);
			
		}
		arquivo.close();
		return dados;
		
	}

	// Método de Inicialização
	@Before
	public void iniciar() {
		url = "https://pt.wikipedia.org";
		System.setProperty("webdriver.chrome.driver", 
		"C:\\Iterasys\\FTS126-workspace\\wiki\\Drivers\\chrome\\83\\chromedriver.exe");
		
		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized"); // open Browser in maximized mode
		options.addArguments("disable-infobars"); // disabling infobars
		options.addArguments("--disable-extensions"); // disabling extensions
		options.addArguments("--disable-gpu"); // applicable to windows os only
		options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
		options.addArguments("--no-sandbox"); // Bypass OS security model
		
		//driver = new ChromeDriver(options);
		
		//aqui começa o firefox
		System.setProperty("webdriver.gecko.driver",
				"C:\\Iterasys\\FTS126-workspace\\wiki\\Drivers\\Firefox\\geckodriver.exe");
		FirefoxOptions optionsff = new FirefoxOptions();
		optionsff.setCapability("marionette", false);
		driver = new FirefoxDriver(optionsff);
		//aqui termina o firefox	
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(60000, TimeUnit.MILLISECONDS);
	}
	
	@After
	public void finalizar() {
		driver.quit();
	}
	
	@Test
	public void buscar() throws IOException, InterruptedException {
		driver.get(url); // abrir o navegador na página alvo
		driver.findElement(By.id("searchInput")).sendKeys(termo);
		// tirar um print
		print("passo 1 consulta pelo termo");
		//driver.findElement(By.cssSelector("div.suggestions-result")).click();
		driver.findElement(By.id("searchInput")).sendKeys(Keys.ENTER);
		
		//driver.findElement(By.cssSelector("div.mw-search-result-heading")).click();
		driver.findElement(By.linkText(resultado)).click();
		Thread.sleep(10000);
		
		assertEquals(resultado, driver.findElement(By.id("firstHeading")).getText());
		print("Passo 2 -Valida Resultado");
	
	}

	
}
