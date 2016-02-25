package fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import com.parse.starter.Feed;
import com.parse.starter.R;
import com.parse.starter.SignUpActivity;

public class CadastrarFragment extends Fragment {

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
    View view = null;
    public CadastrarFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
                    nomeRepresentantePJView.setVisibility(View.GONE);
                    nomeFantasiaPJuridicaView.setVisibility(View.GONE);

                    nomePFisicaView = (EditText) view.findViewById(R.id.campoNome);
                    nomePFisicaView.setVisibility(View.VISIBLE);
                    nomePFisicaView.setHint("Nome");

                    cnpjView = (EditText) view.findViewById(R.id.campoCpfCnpj);
                    cpfView.setHint("CPF");


                } else {
                    Log.i("AppInfo", "SWITCH PRESSINADO: " + mySwitch.isChecked());
                    Toast toast = Toast.makeText(getActivity(), "Comércio selecionado, preencha os campos.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 10, 10);
                    toast.show();

                    //Toast.makeText(getApplicationContext(), "Comércio selecionado, preencha os campos.", Toast.LENGTH_LONG).show();

                    nomePFisicaView.setVisibility(View.GONE);

                    nomeRepresentantePJView = (EditText) view.findViewById(R.id.campoNome);
                    nomeRepresentantePJView.setVisibility(View.VISIBLE);
                    nomeRepresentantePJView.setHint("Nome Representante");

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
                    Toast.makeText(getActivity(), validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    return;
                }

                // Set up a progress dialog
                final ProgressDialog dlg = new ProgressDialog(getActivity());
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
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            // Start an intent for the dispatch activity
                            Intent intent = new Intent(getActivity(), DispatchActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

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

}
