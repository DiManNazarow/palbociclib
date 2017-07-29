package ru.mbg.palbociclib.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;

import ru.mbg.palbociclib.R;


public class PreparatFragment extends Fragment {

    public PreparatFragment() {}

    public static PreparatFragment newInstance() {
        return new PreparatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_preparat, container, false);

        PDFView pdfView = (PDFView) v.findViewById(R.id.pdf_view);
        pdfView.fromAsset("preparat.pdf").load();
        pdfView.fitToWidth();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.title_preparat);
    }
}
