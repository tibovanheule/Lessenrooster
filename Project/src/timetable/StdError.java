package timetable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class StdError {
    public StdError(String error) {
        try (BufferedWriter errorWriter = new BufferedWriter(new OutputStreamWriter(System.err))) {
            errorWriter.append(error);
            /*errorWriter.write(error);*/
            errorWriter.flush();

        } catch (IOException errorWhileGivingAErrorLol) {
            errorWhileGivingAErrorLol.printStackTrace();
        }
    }
}
