package Quiz;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReportGeneration {

    // ANSI escape codes for coloring
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";

    /**
     * Generates and displays a formatted report of the quiz results.
     *
     * @param username         the username of the participant
     * @param schoolRegNo      the school registration number of the participant
     * @param totalTime        the total time taken for the quiz in milliseconds
     * @param questionAttempts a list of QuestionAttempt objects containing quiz details
     */
    public static void generateReport(String username, String schoolRegNo, long totalTime, List<QuestionAttempt> questionAttempts) {
        // Print report heading
        System.out.println("\n" + ANSI_CYAN + "Challenge Report" + ANSI_RESET);
        System.out.println("=================");

        // Print participant details
        System.out.printf("%-30s: %s\n", "Username", username);
        System.out.printf("%-30s: %s\n", "School Registration Number", schoolRegNo);
        System.out.printf("%-30s: %d seconds\n", "Total Time Taken", totalTime / 1000);
        System.out.println();

        // Define column widths
        int questionWidth = 50;
        int answerWidth = 20;
        int correctAnswerWidth = 20;
        int scoreWidth = 10;
        int timeTakenWidth = 10;

        // Print table headers with proper spacing
        System.out.printf("%-" + questionWidth + "s | %-" + answerWidth + "s | %-" + correctAnswerWidth + "s | %-" + scoreWidth + "s | %-" + timeTakenWidth + "s\n",
                ANSI_CYAN + "Question" + ANSI_RESET,
                ANSI_CYAN + "Your Answer" + ANSI_RESET,
                ANSI_CYAN + "Correct Answer" + ANSI_RESET,
                ANSI_CYAN + "Score" + ANSI_RESET,
                ANSI_CYAN + "Time Taken" + ANSI_RESET);

        // Print a separator line
        System.out.println(new String(new char[questionWidth + answerWidth + correctAnswerWidth + scoreWidth + timeTakenWidth + 13]).replace("\0", "-"));

        // Print each question attempt with adjusted formatting
        for (QuestionAttempt attempt : questionAttempts) {
            System.out.printf("%-" + questionWidth + "s | %-" + answerWidth + "s | %-" + correctAnswerWidth + "s | %-" + scoreWidth + "d | %-" + timeTakenWidth + "d\n",
                    truncate(attempt.getQuestionText(), questionWidth),
                    truncate(attempt.getGivenAnswer(), answerWidth),
                    truncate(attempt.getCorrectAnswer(), correctAnswerWidth),
                    attempt.getScore(),
                    attempt.getTimeTaken() / 1000); // Convert milliseconds to seconds
        }

        // Print ending message
        System.out.println();
        System.out.println(ANSI_CYAN + "Thank you for participating in the Mathematics Challenge!" + ANSI_RESET);
    }

    /**
     * Truncates a string to the specified width.
     *
     * @param str   the string to truncate
     * @param width the width to truncate to
     * @return the truncated string
     */
    private static String truncate(String str, int width) {
        if (str.length() <= width) {
            return str;
        } else {
            return str.substring(0, width - 3) + "...";
        }
    }

    /**
     * Generates a PDF report with questions and their correct answers.
     *
     * @param filePath         the file path to save the PDF report
     * @param questionAttempts a list of QuestionAttempt objects containing quiz details
     * @throws DocumentException if an error occurs during PDF creation
     * @throws IOException       if an I/O error occurs
     */
    public static void generatePDFReport(String filePath, List<QuestionAttempt> questionAttempts) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Font.BOLD);

        Paragraph title = new Paragraph("Challenge Report", boldFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{70, 30});

        table.addCell(new Paragraph("Question", boldFont));
        table.addCell(new Paragraph("Correct Answer", boldFont));

        for (QuestionAttempt attempt : questionAttempts) {
            table.addCell(new Paragraph(attempt.getQuestionText(), font));
            table.addCell(new Paragraph(attempt.getCorrectAnswer(), font));
        }

        document.add(table);
        document.close();
    }
}
