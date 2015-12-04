package com.uan.jossuesl.farmacia;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class BuscarActivity extends Activity {

    private EditText etNombre;
    private String etTabla;
    //private EditText etTabla;
    private TextView tvCodigo;
    private TextView tvFormula;
    private TextView tvTipo;
    private TextView tvPrecio;
    private Button btnConectar;

    private String IP = "192.100.162.93:3306";
    private String baseDatos = "/jossue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        etNombre = (EditText)findViewById(R.id.etNombre);
        etTabla = "producto";
        //etTabla = (EditText)findViewById(R.id.etTabla);
        tvCodigo = (TextView)findViewById(R.id.tvCodigo);
        tvTipo = (TextView)findViewById(R.id.tvTipo);
        tvFormula = (TextView)findViewById(R.id.tvFormula);
        tvPrecio = (TextView)findViewById(R.id.tvPrecio);
        btnConectar = (Button)findViewById(R.id.btnInsertar);

        btnConectar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String tabla = etTabla;
                //String tabla = etTabla.getText().toString();
                String nombre = etNombre.getText().toString();

                if (tabla.equals("New Nintendo3DS")){
                    tabla = "`" + tabla + "`";
                }
                new ConexionDB().execute(IP,baseDatos,tabla,nombre);
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

    public class ConexionDB extends AsyncTask<String,Void,ResultSet>{

        @Override
        protected ResultSet doInBackground(String... strings) {

            try {
                Connection conn;
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://"+strings[0]+strings[1], "jossue", "economia");

                Statement estado = conn.createStatement();
                System.out.println("Conexion establecida");
                String peticion ="select * from " +strings[2]+" where nombre='"+strings[3]+"'";
                ResultSet result = estado.executeQuery(peticion);
                return result;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ResultSet result) {

            try {
                if (result != null){
                    if (!result.next()) {
                        Toast toast = Toast.makeText(getApplicationContext(),"No existen resultados con ese nombre",Toast.LENGTH_LONG);
                        toast.show();
                    }else{
                        tvCodigo.setText(result.getString("codigo_producto"));
                        tvFormula.setText(result.getString("formula"));
                        tvTipo.setText(result.getString("tipo"));
                        tvPrecio.setText(Float.toString(result.getFloat("precio")));
                    }
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),"El medicamento no está en la base de datos",Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}