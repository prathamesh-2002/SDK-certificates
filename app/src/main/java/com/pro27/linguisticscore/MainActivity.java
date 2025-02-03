 package com.pro27.linguisticscore;

 import android.graphics.Bitmap;
 import android.graphics.Canvas;
 import android.graphics.Color;
 import android.graphics.Paint;
 import android.graphics.pdf.PdfDocument;
 import android.os.Bundle;
 import android.os.Environment;
 import android.view.View;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.Toast;

 import androidx.appcompat.app.AppCompatActivity;

 import java.io.File;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 import java.util.Locale;

 public class MainActivity extends AppCompatActivity {
     private EditText nameInput;
     private EditText classInput;
     private EditText scoreInput;
     private Button generateButton;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         // Initialize views
         nameInput = findViewById(R.id.nameInput);
         classInput = findViewById(R.id.classInput);
         scoreInput = findViewById(R.id.scoreInput);
         generateButton = findViewById(R.id.generateButton);

         generateButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 generateCertificate();
             }
         });
     }

     private void generateCertificate() {
         String name = nameInput.getText().toString();
         String className = classInput.getText().toString();
         String scoreStr = scoreInput.getText().toString();

         if (name.isEmpty() || className.isEmpty() || scoreStr.isEmpty()) {
             Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
             return;
         }

         int score = Integer.parseInt(scoreStr);
         if (score < 0 || score > 100) {
             Toast.makeText(this, "Score must be between 0 and 100", Toast.LENGTH_SHORT).show();
             return;
         }

         // Create PDF document
         PdfDocument document = new PdfDocument();
         PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200, 800, 1).create();
         PdfDocument.Page page = document.startPage(pageInfo);

         // Get canvas for drawing
         Canvas canvas = page.getCanvas();

         // Draw certificate template
         Bitmap template = getTemplateBitmap();
         canvas.drawBitmap(template, 0, 0, null);

         // Setup text paint
         Paint paint = new Paint();
         paint.setColor(Color.BLACK);
         paint.setTextSize(50);
         paint.setTextAlign(Paint.Align.CENTER);

         // Draw student information
         canvas.drawText(name, 600, 400, paint);
         canvas.drawText("Class: " + className, 600, 480, paint);
         canvas.drawText("Score: " + score + "%", 600, 560, paint);

         // Add date
         SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
         String date = dateFormat.format(new Date());
         paint.setTextSize(30);
         canvas.drawText("Date: " + date, 600, 640, paint);

         document.finishPage(page);

         // Save PDF to external storage
         String fileName = "Certificate_" + name.replace(" ", "_") + ".pdf";
         File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);

         try {
             document.writeTo(new FileOutputStream(file));
             Toast.makeText(this, "Certificate saved to Downloads folder", Toast.LENGTH_LONG).show();
         } catch (IOException e) {
             e.printStackTrace();
             Toast.makeText(this, "Error saving certificate", Toast.LENGTH_SHORT).show();
         }

         document.close();
     }

     private Bitmap getTemplateBitmap() {
         // Create a bitmap for the template
         Bitmap template = Bitmap.createBitmap(1200, 800, Bitmap.Config.ARGB_8888);
         Canvas canvas = new Canvas(template);

         // Draw background
         Paint paint = new Paint();
         paint.setColor(Color.rgb(255, 248, 220)); // Cream color
         canvas.drawRect(0, 0, 1200, 800, paint);

         // Draw border
         paint.setStyle(Paint.Style.STROKE);
         paint.setColor(Color.rgb(139, 69, 19)); // Brown color
         paint.setStrokeWidth(20);
         canvas.drawRect(40, 40, 1160, 760, paint);

         // Draw header
         paint.setStyle(Paint.Style.FILL);
         paint.setTextSize(80);
         paint.setTextAlign(Paint.Align.CENTER);
         paint.setColor(Color.rgb(139, 69, 19));
         canvas.drawText("Certificate of Achievement", 600, 200, paint);

         // Draw decorative lines
         paint.setStrokeWidth(5);
         canvas.drawLine(200, 250, 1000, 250, paint);
         canvas.drawLine(200, 700, 1000, 700, paint);

         return template;
     }
 }