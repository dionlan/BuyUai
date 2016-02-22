package com.parse.starter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class SignUpActivity extends AppCompatActivity {


    private EditText nomeView;
    private EditText nomePFisicaView;
    private EditText nomeRepresentantePJView;
    private EditText nomeFantasiaPJuridicaView;
    private EditText usuarioView;
    private EditText senhaView;
    private EditText confirmaSenhaView;
    private EditText emailView;
    private EditText cpfView;
    private EditText cnpjView;
    private EditText enderecoView;
    private EditText telefoneView;
    private EditText cepView;
    boolean fotoCamera;
    private Bitmap bitmap;
    ImageView imagemContatoView;
    Uri imagemUri = Uri.parse("android.resource://com.parse.starter/drawable/ic_user.png");

    private Switch mySwitch;
    private TextView switchCpfCnpj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the signup form.
        imagemContatoView = (ImageView) findViewById(R.id.imagemContato);
        nomePFisicaView = (EditText) findViewById(R.id.campoNome);
        nomeFantasiaPJuridicaView = (EditText) findViewById(R.id.campoNomeFantasia);
        cpfView = (EditText) findViewById(R.id.campoCpfCnpj);
        usuarioView = (EditText) findViewById(R.id.campoUsuario);
        senhaView = (EditText) findViewById(R.id.campoSenha);
        confirmaSenhaView = (EditText) findViewById(R.id.campoConfirmaSenha);
        emailView = (EditText) findViewById(R.id.campoEmail);

        enderecoView = (EditText) findViewById(R.id.campoEndereco);
        telefoneView = (EditText) findViewById(R.id.campoTelefone);
        cepView = (EditText) findViewById(R.id.campoCep);

        /**
         * Verificação se é o cadastro de usuário ou comércio
         **/

        mySwitch = (Switch) findViewById(R.id.mySwitch);

        //set the switch to ON
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mySwitch.setChecked(false);

            nomeFantasiaPJuridicaView.setVisibility(View.GONE);

        }
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    nomeRepresentantePJView.setVisibility(View.GONE);
                    nomeFantasiaPJuridicaView.setVisibility(View.GONE);

                    nomePFisicaView = (EditText) findViewById(R.id.campoNome);
                    nomePFisicaView.setVisibility(View.VISIBLE);
                    nomePFisicaView.setHint("Nome");

                    cnpjView = (EditText) findViewById(R.id.campoCpfCnpj);
                    cpfView.setHint("CPF");


                } else {

                    Toast.makeText(getApplicationContext(), "Comércio selecionado, preencha os campos.", Toast.LENGTH_LONG).show();

                    nomePFisicaView.setVisibility(View.GONE);

                    nomeRepresentantePJView = (EditText) findViewById(R.id.campoNome);
                    nomeRepresentantePJView.setVisibility(View.VISIBLE);
                    nomeRepresentantePJView.setHint("Nome Representante");

                    nomeFantasiaPJuridicaView.setVisibility(View.VISIBLE);
                    nomeFantasiaPJuridicaView = (EditText) findViewById(R.id.campoNomeFantasia);
                    nomeFantasiaPJuridicaView.setHint("Nome Fantasia");

                    cnpjView = (EditText) findViewById(R.id.campoCpfCnpj);
                    cnpjView.setHint("CNPJ");
                }
            }
        });



        // Set up the submit button click handler
        findViewById(R.id.action_button_signup).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // Validate the sign up data
                boolean validationError = false;
                StringBuilder validationErrorMessage = new StringBuilder(getResources().getString(R.string.error_intro));
                if(!mySwitch.isChecked()){
                    if (isEmpty(nomePFisicaView)) {
                        validationError = true;
                        validationErrorMessage.append("Informe seu nome");
                    }
                }else{
                    if (isEmpty(nomeRepresentantePJView)) {
                        validationError = true;
                        validationErrorMessage.append("Informe o nome do representante da empresa");
                    }
                    if (isEmpty(nomeFantasiaPJuridicaView)) {
                        validationError = true;
                        validationErrorMessage.append("Informe o nome fantasia do comércio");
                    }
                    if (isEmpty(cnpjView)) {
                        validationError = true;
                        validationErrorMessage.append("Informe seu CNPJ");
                    }
                }
                if (isEmpty(usuarioView)) {
                    validationError = true;
                    validationErrorMessage.append(getResources().getString(R.string.error_blank_username));
                }
                if (isEmpty(senhaView)) {
                    if (validationError) {
                        validationErrorMessage.append(getResources().getString(R.string.error_join));
                    }
                    validationError = true;
                    validationErrorMessage.append(getResources().getString(R.string.error_blank_password));
                }
                if (isEmpty(emailView)) {
                    validationError = true;
                    validationErrorMessage.append("Informe seu e-mail");
                }
                if (isEmpty(cpfView)) {
                    validationError = true;
                    validationErrorMessage.append("Informe seu CPF");
                }
                if (isEmpty(enderecoView)) {
                    validationError = true;
                    validationErrorMessage.append("Informe seu endereço");
                }
                if (isEmpty(telefoneView)) {
                    validationError = true;
                    validationErrorMessage.append("Informe seu telefone");
                }
                if (!isMatching(senhaView, confirmaSenhaView)) {
                    if (validationError) {
                        validationErrorMessage.append(getResources().getString(R.string.error_join));
                    }
                    validationError = true;
                    validationErrorMessage.append(getResources().getString(
                            R.string.error_mismatched_passwords));
                }
                validationErrorMessage.append(getResources().getString(R.string.error_end));

                // If there is a validation error, display the error
                if (validationError) {
                    Toast.makeText(SignUpActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    return;
                }

                // Set up a progress dialog
                final ProgressDialog dlg = new ProgressDialog(SignUpActivity.this);
                dlg.setTitle("Por favor, aguarde...");
                dlg.setMessage("Cadastrando... Por favor, aguarde...");
                dlg.show();

                // Set up a new Parse user
                ParseUser user = new ParseUser();
                if (!mySwitch.isChecked()) { //Cadastro de pessoa física
                    user.put("isComercio", false);
                    user.put("nome", nomePFisicaView.getText().toString());
                    user.put("cpf", cpfView.getText().toString());
                } else { //Cadastro de Pessoa Jurídica
                    user.put("isComercio", true);
                    user.put("nomeFantasia", nomeFantasiaPJuridicaView.getText().toString());
                    user.put("nomeRepresentante", nomeRepresentantePJView.getText().toString());
                    user.put("cnpj", cnpjView.getText().toString());
                }
                //Cadastro comum entre entre PF e PJ
                user.setUsername(usuarioView.getText().toString());
                user.setPassword(senhaView.getText().toString());
                user.put("email", emailView.getText().toString());
                user.put("endereco", enderecoView.getText().toString());
                user.put("telefone", telefoneView.getText().toString());
                user.put("cep", cepView.getText().toString());

                byte[] braveData = (imagemContatoView.toString()).getBytes();
                ParseFile imgFile = new ParseFile(nomePFisicaView.getText().toString() + "_" + "Usuario.png", braveData);
                imgFile.saveInBackground();
                user.put("foto", imgFile);

                // Call the Parse signup method
                user.signUpInBackground(new SignUpCallback() {

                    @Override
                    public void done(ParseException e) {
                        dlg.dismiss();
                        if (e != null) {
                            // Show the error message
                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            // Start an intent for the dispatch activity
                            Intent intent = new Intent(SignUpActivity.this, DispatchActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isMatching(EditText etText1, EditText etText2) {
        if (etText1.getText().toString().equals(etText2.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    public void clicaCarregarImagem(View v) {
        fotoCamera = false;
        Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), 1);
    }

    public void clicaTirarFoto(View v) {
        fotoCamera = true;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (fotoCamera) {
            super.onActivityResult(requestCode, resultCode, data);
            InputStream stream = null;
            if (requestCode == 0 && resultCode == RESULT_OK) {
                try {
                    if (bitmap != null) {
                        bitmap.recycle();
                    }

                    stream = getContentResolver().openInputStream(data.getData());
                    if (stream == null) {
                        bitmap = (Bitmap) data.getExtras().get("data");
                    } else {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    }

                    bitmap = BitmapFactory.decodeStream(stream);
                    imagemContatoView.setImageBitmap(resizeImage(this, bitmap, 120, 120));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        //imagemContatoView.setRotation(90);
                    }
                    imagemUri = data.getData();
                    imagemContatoView.setImageURI(data.getData());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (stream != null)
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }

            }
        } else {

            if (resultCode == RESULT_OK) {
                if (requestCode == 1) {
                    imagemUri = data.getData();
                    imagemContatoView.setImageURI(data.getData());
                }
            }
        }
    }

    private static Bitmap resizeImage(Context context, Bitmap bmpOriginal, float newWidth, float newHeight) {
        Bitmap novoBmp = null;
        int w = bmpOriginal.getWidth();
        int h = bmpOriginal.getHeight();
        float densityFactor = context.getResources().getDisplayMetrics().density;
        float novoW = newWidth * densityFactor;
        float novoH = newHeight * densityFactor;
        //Calcula escala em percentagem do tamanho original para o novo tamanho
        float scalaW = novoW / w;
        float scalaH = novoH / h;
        // Criando uma matrix para manipulação da imagem BitMap
        Matrix matrix = new Matrix();
        // Definindo a proporção da escala para o matrix
        matrix.postScale(scalaW, scalaH);
        //criando o novo BitMap com o novo tamanho
        novoBmp = Bitmap.createBitmap(bmpOriginal, 0, 0, w, h, matrix, true);
        return novoBmp;

    }
}
