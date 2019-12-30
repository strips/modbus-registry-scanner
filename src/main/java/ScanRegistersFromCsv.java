import com.opencsv.CSVReader;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import de.re.easymodbus.exceptions.FunctionCodeNotSupportedException;
import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ScanRegistersFromCsv {
    public static void main(String[] args) {


        try {
            FileReader fileReader = new FileReader("D:\\home\\strips\\Documents\\systemair.csv");

            CSVReader reader = new CSVReader(fileReader);
            List<String[]> csvFileList = reader.readAll();

            System.out.println("Values = " + csvFileList.size());

            ModbusClient modbusClient = new ModbusClient("10.10.100.18", 502);
            modbusClient.Connect();

            for (int i = 0; i < csvFileList.size(); i++) {
                String[] csvLineArray = csvFileList.get(i);
                if (i > 0) {
                    String registerCode = csvLineArray[0];
                    String signed = csvLineArray[1];
                    String registerType = csvLineArray[2];
                    String registerFlag = csvLineArray[3];
                    Integer address = Integer.parseInt(csvLineArray[4]);
                    String min = csvLineArray[5].replaceAll("\n", "");
                    String max = csvLineArray[6].replaceAll("\n", "");
                    String description = csvLineArray[7];

                    Integer value = null;
                    String status = "";

                    if (registerType.contains("Read Input Register")) {
                        try {
                            value = modbusClient.ReadInputRegisters(address, 1)[0];
                            status = "OK";
                        } catch (FunctionCodeNotSupportedException e) {
                            status = "ReadInputRegisters returned FunctionCodeNotSupportedException, try ReadHoldingRegisters";
                        }
                    }

                    if ((registerType.contains("Holding Register")) || (!status.isEmpty())) {
                        value = modbusClient.ReadHoldingRegisters(address, 1)[0];
                    }

                    System.out.println(("\"" + registerCode + "\",\"" + signed + "\",\"" + registerType + "\",\"" + registerFlag + "\",\"" + address + "\",\"" + min + "\",\"" + max + "\",\"" + description + "\",\"" + value + "\",\"" + status + "\"").replaceAll("\"\"",""));
                } else {
                    System.out.println(java.util.Arrays.toString(csvLineArray).replaceAll("\\[|\\]|\\s","") + ",value,status");
                }
            }

            modbusClient.Disconnect();



        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}