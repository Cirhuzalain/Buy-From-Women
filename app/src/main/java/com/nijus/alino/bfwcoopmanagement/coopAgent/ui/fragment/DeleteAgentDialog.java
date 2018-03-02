package com.nijus.alino.bfwcoopmanagement.coopAgent.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coopAgent.sync.DeleteSyncAgentBkgrnd;

import java.util.ArrayList;

public class DeleteAgentDialog extends DialogFragment implements DialogInterface.OnClickListener, View.OnClickListener {

    private ArrayList<Integer> agentIds;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        View viewContainer = getActivity().getLayoutInflater().inflate(R.layout.delete_agent, null);

        TextView msgTextView = viewContainer.findViewById(R.id.msg);

        Bundle bundle = getArguments();
        if (bundle != null) {
            agentIds = getArguments().getIntegerArrayList("agent_ids");

            String print_msg = msgTextView.getText() + " " + agentIds.size() + " item(s) ? ";
            msgTextView.setText(print_msg);
        }


        Button confirm = viewContainer.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);

        builder.setView(viewContainer)
                .setNegativeButton(R.string.msg_cancel, this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), DeleteSyncAgentBkgrnd.class);
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList("agent_del", agentIds);
        intent.putExtra("agent_data", bundle);
        getContext().startService(intent);
        dismiss();
    }
}
