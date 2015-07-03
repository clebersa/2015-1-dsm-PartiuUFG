package br.ufg.inf.es.dsm.partiuufg.fragment.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.activity.CampusActivity;
import br.ufg.inf.es.dsm.partiuufg.dbModel.Campus;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusLine;

/**
 * Created by Bruno on 02/07/2015.
 */
public class CampiPageFragment extends Fragment {
    public static final String CAMPUS_SAMAMBAIA_NAME = "Campus Samambaia";
    public static final String CAMPUS_COLEMAR_NAME = "Campus Colemar Natal e Silva";
    private HashMap<String, Campus> campi;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.home_page_campi,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Campus> campiList = Campus.listAll(Campus.class);

        boolean hasSamambaia = false;
        boolean hasColemar = false;
        campi = new HashMap<>();
        for( Campus campus : campiList ) {
            if(CAMPUS_SAMAMBAIA_NAME.equals(campus.getName())) {
                campi.put(CAMPUS_SAMAMBAIA_NAME, campus);
                hasSamambaia = true;
            } else if (CAMPUS_COLEMAR_NAME.equals(campus.getName())) {
                campi.put(CAMPUS_COLEMAR_NAME, campus);
                hasColemar = true;
            }
        }

        if(!hasSamambaia) initCampusSamambaia();
        if(!hasColemar) initCampusColemar();
    }

    private void initCampusSamambaia(){
        Campus campusSamambaia = new Campus(CAMPUS_SAMAMBAIA_NAME);
        campusSamambaia.save();
        campi.put(CAMPUS_SAMAMBAIA_NAME, campusSamambaia);

        initCampus(campusSamambaia, new int[]{105, 270, 174, 263, 268, 269, 302});
    }

    private void initCampusColemar(){
        Campus campusColemar = new Campus(CAMPUS_COLEMAR_NAME);
        campusColemar.save();
        campi.put(CAMPUS_COLEMAR_NAME, campusColemar);

        initCampus(campusColemar, new int[]{19, 26, 302});
    }

    private void initCampus(Campus campus, int[] lineNumbers){
        SingleBusLine singleBusLine;
        List<SingleBusLine> singleBusLineList;
        for(Integer lineNumber: lineNumbers){
            singleBusLineList = SingleBusLine.find(SingleBusLine.class, "number = ?",
                    lineNumber.toString());

            if(singleBusLineList == null || singleBusLineList.size() == 0){
                singleBusLine = new SingleBusLine(lineNumber);
                singleBusLine.setCampus(campus);
                singleBusLine.save();
            }else{
                boolean hasCampus = false;
                for(SingleBusLine sbl: singleBusLineList){
                    if(sbl.getCampus().getId() == campus.getId()){
                        hasCampus = true;
                        break;
                    }
                }
                if(!hasCampus){
                    singleBusLine = new SingleBusLine(lineNumber);
                    singleBusLine.setCampus(campus);
                    singleBusLine.save();
                }
            }
        }
    }

    public HashMap<String, Campus> getCampi() {
        return campi;
    }

    public void loadBusLines(Campus campus){
        Intent intent = new Intent(getActivity(), CampusActivity.class);
        intent.putExtra("campusId", campus.getId());
        this.startActivity(intent);
    }
}
