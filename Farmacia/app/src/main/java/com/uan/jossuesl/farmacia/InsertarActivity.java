package com.uan.jossuesl.farmacia;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class InsertarActivity extends Activity {
    private String etTabla;
    //private EditText etTabla;
    private EditText etNombre;
    //private EditText etCodigo;
    private EditText etFormula;
    private EditText etTipo;
    private EditText etPrecio;
    private EditText etProveedor;
    private Button btnInsertar;

    private String IP = "192.100.162.93:3306";
    private String baseDatos = "/jossue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar);

        etNombre = (EditText)findViewById(R.id.etNombre);
        etTabla = "producto";
        //etTabla = (EditText)findViewById(R.id.etTabla);
        //etCodigo = (EditText)findViewById(R.id.etCodigo);
        etTipo = (EditText)findViewById(R.id.etTipo);
        etFormula = (EditText)findViewById(R.id.etFormula);
        etPrecio = (EditText)findViewById(R.id.etPrecio);
        etProveedor = (EditText) findViewById(R.id.etCodigoP);
        btnInsertar = (Button)findViewById(R.id.btnInsertar);


        btnInsertar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String tabla = etTabla;
                //String tabla = etTabla.getText().toString();
                String nombre = etNombre.getText().toString();
                //String codigo = etCodigo.getText().toString();
                String tipo = etTipo.getText().toString();
                String formula = etFormula.getText().toString();
                String precio = etPrecio.getText().toString();
                String proveedor = etProveedor.getText().toString();

                if (tabla.equals("New Nintendo3DS")) {
                    tabla = "`" + tabla + "`";
                }
                new ConexionDB().execute(IP, baseDatos, tabla, nombre, tipo, formula, precio, proveedor);
                Toast toast1;
                toast1 = Toast.makeText(getApplicationContext(), "El medicamento ha sido ingresado correctamente", Toast.LENGTH_LONG);
                toast1.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ConexionDB extends AsyncTask<String, Void, Integer> implements com.uan.jossuesl.farmacia.ConexionDB {

        @Override
        protected Integer doInBackground(String... strings) {

            try {
                Connection conn;
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://"+strings[0]+strings[1], "jossue", "economia");

                Statement estado = conn.createStatement();
                System.out.println("Conexion establecida");
                String peticion ="INSERT INTO " +strings[2]+"(`nombre`, `formula`, `tipo`, `precio`, `codigo_proveedor`) VALUES('"+strings[3]+"', '"+strings[5]+"', '"+strings[4]+"', '"+strings[6]+"','"+strings[7]+"')";
                int result = estado.executeUpdate(peticion);
                return result;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}