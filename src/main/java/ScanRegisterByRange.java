import de.re.easymodbus.exceptions.FunctionCodeNotSupportedException;
import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;

import java.io.IOException;

public class ScanRegisterByRange {
    public static void main(String[] args) {
        ModbusClient modbusClient = new ModbusClient("10.10.100.18", 502);
        try {
            modbusClient.Connect();

            for (int i = 1000; i < 1020; i++) {

                // Test retrieving HoldingRegister
                try {
                    System.out.println("HoldingRegister\t" + i + "\t" + modbusClient.ReadHoldingRegisters(i, 1)[0]);
                } catch (FunctionCodeNotSupportedException functionCodeNotSupportedException) {
                    // This address is not of this registry type
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Test retrieving InputRegister
                try {
                    System.out.println("InputRegister\t" + i + "\t" + modbusClient.ReadInputRegisters(i, 1)[0]);
                } catch (FunctionCodeNotSupportedException functionCodeNotSupportedException) {
                    // This address is not of this registry type
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            modbusClient.Disconnect();
        } catch (IOException e) {
            System.out.println("IOException ERROR");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Unknown exception ERROR");
            e.printStackTrace();
        }

    }
}