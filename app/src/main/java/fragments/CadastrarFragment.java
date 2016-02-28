package fragments;

import android.app.Activity;
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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.starter.DispatchActivity;
import com.parse.starter.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class CadastrarFragment extends Fragment {

    private EditText nomeView;
    private EditText nomePFisicaView;
    private EditText razaoSocialPJView;
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
    View view = null;
    public CadastrarFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cadastrar, container, false);

        // Set up the signup form.
        imagemContatoView = (ImageView) view.findViewById(R.id.imagemContato);
        nomePFisicaView = (EditText) view.findViewById(R.id.campoNome);
        nomeFantasiaPJuridicaView = (EditText) view.findViewById(R.id.campoNomeFantasia);
        cpfView = (EditText) view.findViewById(R.id.campoCpfCnpj);
        usuarioView = (EditText) view.findViewById(R.id.campoUsuario);
        senhaView = (EditText) view.findViewById(R.id.campoSenha);
        confirmaSenhaView = (EditText) view.findViewById(R.id.campoConfirmaSenha);
        emailView = (EditText) view.findViewById(R.id.campoEmail);
        enderecoView = (EditText) view.findViewById(R.id.campoEndereco);
        telefoneView = (EditText) view.findViewById(R.id.campoTelefone);
        cepView = (EditText) view.findViewById(R.id.campoCep);


        mySwitch = (Switch) view.findViewById(R.id.mySwitch);

        //set the switch to ON
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mySwitch.setChecked(false);

            nomeFantasiaPJuridicaView.setVisibility(View.GONE);

        }
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    razaoSocialPJView.setVisibility(View.GONE);
                    nomeFantasiaPJuridicaView.setVisibility(View.GONE);

                    nomePFisicaView = (EditText) view.findViewById(R.id.campoNome);
                    nomePFisicaView.setVisibility(View.VISIBLE);
                    nomePFisicaView.setHint("Nome");

                    cnpjView = (EditText) view.findViewById(R.id.campoCpfCnpj);
                    cpfView.setHint("CPF");


                } else {
                    Log.i("AppInfo", "SWITCH PRESSINADO: " + mySwitch.isChecked());
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Comércio selecionado, preencha os campos.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 10, 10);
                    toast.show();

                    //Toast.makeText(getApplicationContext(), "Comércio selecionado, preencha os campos.", Toast.LENGTH_LONG).show();

                    nomePFisicaView.setVisibility(View.GONE);

                    razaoSocialPJView = (EditText) view.findViewById(R.id.campoNome);
                    razaoSocialPJView.setVisibility(View.VISIBLE);
                    razaoSocialPJView.setHint("Nome Representante");

                    nomeFantasiaPJuridicaView.setVisibility(View.VISIBLE);
                    nomeFantasiaPJuridicaView = (EditText) view.findViewById(R.id.campoNomeFantasia);
                    nomeFantasiaPJuridicaView.setHint("Nome Fantasia");

                    cnpjView = (EditText) view.findViewById(R.id.campoCpfCnpj);
                    cnpjView.setHint("CNPJ");
                }
            }
        });

        // Set up the submit button click handler
        view.findViewById(R.id.action_button_signup).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // Validate the sign up data
                boolean validationError = false;
                StringBuilder validationErrorMessage = new StringBuilder(getResources().getString(R.string.error_intro));
                if (!mySwitch.isChecked()) {
                    if (isEmpty(nomePFisicaView)) {
                        validationError = true;
                        validationErrorMessage.append("Informe seu nome");
                    }
                } else {
                    if (isEmpty(razaoSocialPJView)) {
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
                    Toast.makeText(getActivity().getApplicationContext(), validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    return;
                }

                // Set up a progress dialog
                final ProgressDialog dlg = new ProgressDialog(getActivity().getApplicationContext());
                dlg.setTitle("Por favor, aguarde...");
                dlg.setMessage("Cadastrando... Por favor, aguarde...");
                dlg.show();

                // Set up a new Parse user
                ParseUser user = new ParseUser();
                if (!mySwitch.isChecked()) { //Cadastro de pessoa física
                    user.put("isComercio", false);
                    user.put("nomePessoaFisica", nomePFisicaView.getText().toString());
                    user.put("cpf", cpfView.getText().toString());
                } else { //Cadastro de Pessoa Jurídica
                    user.put("isComercio", true);
                    user.put("nomeFantasia", nomeFantasiaPJuridicaView.getText().toString());
                    user.put("razaoSocial", razaoSocialPJView.getText().toString());
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
                            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            // Start an intent for the dispatch activity
                            Intent intent = new Intent(getActivity().getApplicationContext(), DispatchActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        view.findViewById(R.id.botaoAddFoto).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                fotoCamera = false;
                Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), 1);

            } });

        view.findViewById(R.id.botaoTirarFoto).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                fotoCamera = true;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);

            } });

        // Defines the xml file for the fragment
        //View view = inflater.inflate(R.layout.fragment_cadastrar, container, false);
        // Setup handles to view objects here
        // etFoo = (EditText) view.findViewById(R.id.etFoo);
        //Intent intent = new Intent(getActivity(), SignUpActivity.class);
        //startActivity(intent);
        return view;
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

    @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Context context = null;
        Activity activity = (Activity) context;

        if (fotoCamera) {
            super.onActivityResult(requestCode, resultCode, data);
            InputStream stream = null;
            if (requestCode == 0 && resultCode == -1) {
                try {
                    if (bitmap != null) {
                        bitmap.recycle();
                    }

                    stream = activity.getContentResolver().openInputStream(data.getData());
                    if (stream == null) {
                        bitmap = (Bitmap) data.getExtras().get("data");
                    } else {
                        bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), data.getData());
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

            if (resultCode == -1) {
                if (requestCode == 1) {
                    imagemUri = data.getData();
                    imagemContatoView.setImageURI(data.getData());
                }
            }
        }
    }

    private static Bitmap resizeImage(CadastrarFragment context, Bitmap bmpOriginal, float newWidth, float newHeight) {
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
