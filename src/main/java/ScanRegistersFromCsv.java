import com.opencsv.CSVReader;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import de.re.easymodbus.exceptions.FunctionCodeNotSupportedException;
import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;

import java.io.*;
import java.util.List;

public class ScanRegistersFromCsv {
    public static void main(String[] args) {


        try {
            FileReader fileReader = new FileReader("resources/systemair_SAVE_Modbus_Variable_List_20190116_r29.csv");
            FileWriter csvVariablesWithValues = new FileWriter("resources/modbus_variables_values.csv");


            CSVReader reader = new CSVReader(fileReader);
            List<String[]> csvFileList = reader.readAll();

            System.out.println("Values = " + csvFileList.size());

            ModbusClient modbusClient = new ModbusClient("10.10.100.18", 502);
            modbusClient.Connect();

            String registerCode;
            String signed;
            String registerType;
            String registerFlag;
            Integer address;
            String min;
            String max;
            String description;

            Integer value = null;
            String status = "";

            for (int i = 0; i < csvFileList.size(); i++) {
                String[] csvLineArray = csvFileList.get(i);
                if (i > 0) {

                    registerCode = csvLineArray[0];
                    signed = csvLineArray[1];
                    registerType = csvLineArray[2];
                    registerFlag = csvLineArray[3];
                    address = Integer.parseInt(csvLineArray[4]);
                    min = csvLineArray[5].replaceAll("\n", "");
                    max = csvLineArray[6].replaceAll("\n", "");
                    description = csvLineArray[7];

                    value = null;
                    status = "";

                    if (registerType.contains("Read Input Register")) {
                        try {
                            value = modbusClient.ReadInputRegisters(address, 1)[0];
                            status = "OK";
                        } catch (FunctionCodeNotSupportedException e) {
                            // Workaround, either the server does not match the variable list or it seems InputRegisters can be read as HoldingRegisters
                            status = "ReadInputRegisters returned FunctionCodeNotSupportedException, trying ReadHoldingRegisters";
                        }
                    }

                    // Get HoldingRegister if HoldingRegister or status is not empty, if status is not empty InputRegister query failed
                    if ((registerType.contains("Holding Register")) || (!status.isEmpty())) {
                        value = modbusClient.ReadHoldingRegisters(address, 1)[0];
                    }

                    System.out.println(("\"" + registerCode + "\",\"" + signed + "\",\"" + registerType + "\",\"" + registerFlag + "\",\"" + address + "\",\"" + min + "\",\"" + max + "\",\"" + description + "\",\"" + value + "\",\"" + status + "\"").replaceAll("\"\"",""));
                    csvVariablesWithValues.append(("\"" + registerCode + "\",\"" + signed + "\",\"" + registerType + "\",\"" + registerFlag + "\",\"" + address + "\",\"" + min + "\",\"" + max + "\",\"" + description + "\",\"" + value + "\",\"" + status + "\"").replaceAll("\"\"","") + "\n");

                } else {
                    System.out.println(java.util.Arrays.toString(csvLineArray).replaceAll("\\[|\\]|\\s","") + ",value,status");
                    csvVariablesWithValues.write(java.util.Arrays.toString(csvLineArray).replaceAll("\\[|\\]|\\s","") + ",value,status\n");
                }
                csvVariablesWithValues.flush();
            }

            csvVariablesWithValues.close();
            modbusClient.Disconnect();



        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}