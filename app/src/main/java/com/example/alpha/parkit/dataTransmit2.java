package com.example.alpha.parkit;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class dataTransmit2 extends Fragment {

    private void getData(){
        final ProgressDialog loading;
        loading = new ProgressDialog(getContext());
        loading.setMessage(Html.fromHtml("<b><h2>Retrieving data...."));
        loading.show();
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        loading.hide();
                        if (task.isSuccessful()) {
                            if (!task.getResult().exists()) return;
                            Map<String, Object> jsonObject = task.getResult().getData();
                            if (jsonObject.containsKey("uname")) {
                                //fname.setText(jsonObject.get("uname").toString());
                            }
                            if (jsonObject.containsKey("mail")) {
                                //edit_email.setText(jsonObject.get("mail").toString());
                                //edit_email.setKeyListener(null);
                            }
                            if (jsonObject.containsKey("plot")) {
                                //plotno.setText(jsonObject.get("plot").toString());
                            }
                            if (jsonObject.containsKey("street")) {
                                //street.setText(jsonObject.get("street").toString());
                            }
                            if (jsonObject.containsKey("landmark")) {
                                //landmark.setText(jsonObject.get("landmark").toString());
                            }
                            if (jsonObject.containsKey("city")) {
                                //city.setText(jsonObject.get("city").toString());
                            }
                            if (jsonObject.containsKey("state")) {
                                //state.setText(jsonObject.get("state").toString());
                            }
                            if (jsonObject.containsKey("pin")) {
                                //pincode.setText(jsonObject.get("pin").toString());
                            }
                        } else {
                            Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void register(String... params) {

        final ProgressDialog loading;
        loading = new ProgressDialog(getContext());
        loading.setMessage(Html.fromHtml("<b><h2>Please Wait...."));
        loading.show();
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);


        HashMap<String, String> data = new HashMap();
        data.put("uname", params[0]);
        //data.put("mail", edit_email.getText().toString());
        data.put("plot", params[1]);
        data.put("street", params[2]);
        data.put("landmark", params[3]);
        data.put("state", params[4]);
        data.put("city", params[5]);
        data.put("pin", params[6]);

        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(data, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loading.hide();
                        if(task.isSuccessful())Toast.makeText(getContext(),"sucess",Toast.LENGTH_SHORT).show();
                        else Toast.makeText(getContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
