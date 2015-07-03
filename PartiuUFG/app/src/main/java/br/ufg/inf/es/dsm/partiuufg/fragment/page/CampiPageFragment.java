package br.ufg.inf.es.dsm.partiuufg.fragment.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.dsm.partiuufg.R;
import br.ufg.inf.es.dsm.partiuufg.adapter.recyclerView.CampusSingleBusLinesAdapter;
import br.ufg.inf.es.dsm.partiuufg.dbModel.Campus;
import br.ufg.inf.es.dsm.partiuufg.dbModel.SingleBusLine;
import br.ufg.inf.es.dsm.partiuufg.http.EasyBusService;
import br.ufg.inf.es.dsm.partiuufg.http.RestBusServiceFactory;
import br.ufg.inf.es.dsm.partiuufg.model.BusLine;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Bruno on 02/07/2015.
 */
public class CampiPageFragment extends Fragment {
    public static final String TAG = CampiPageFragment.class.getSimpleName();
    public static final String CAMPUS_SAMAMBAIA_NAME = "Campus Samambaia";
    public static final String CAMPUS_COLEMAR_NAME = "Campus Colemar Natal e Silva";

    private ArrayList<Campus> campi;
    private List<Object> singleBusLineList;
    private SuperRecyclerView recList;
    private CampusSingleBusLinesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.home_page_campi,container,false);

        recList = (SuperRecyclerView) v.findViewById(R.id.rec_list);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        layout.scrollToPosition(0);
        recList.setLayoutManager(layout);
        recList.getRecyclerView().setHasFixedSize(true);

        createCampi();
        pupulateList();
        adapter = new CampusSingleBusLinesAdapter(singleBusLineList, getActivity());
        recList.setAdapter(adapter);

        return v;
    }

    public void pupulateList() {
        singleBusLineList = new ArrayList<>();
        for(Campus campus : campi) {
            singleBusLineList.add(campus);

            List<SingleBusLine> itens = SingleBusLine.find(SingleBusLine.class,
                    "campus = ?", campus.getId().toString());

            for(final SingleBusLine singleBusLine: itens){
                singleBusLineList.add(singleBusLine);

                if(singleBusLine.getName() == null){
                    EasyBusService ebs = RestBusServiceFactory.getAdapter();
                    ebs.getBusLine(singleBusLine.getNumber().toString(), new Callback<BusLine>() {
                        @Override
                        public void success(BusLine busLine, Response response) {
                            singleBusLine.setName(busLine.getName());
                            adapter.notifyDataSetChanged();
                            singleBusLine.save();
                        }

                        @Override
                        public void failure(RetrofitError error) {}
                    });
                }
            }
        }
    }

    public void createCampi() {
        List<Campus> campiList = Campus.listAll(Campus.class);

        boolean hasSamambaia = false;
        boolean hasColemar = false;
        campi = new ArrayList<>();
        for(Campus campus : campiList) {
            if(CAMPUS_SAMAMBAIA_NAME.equals(campus.getName())) {
                campi.add(campus);
                hasSamambaia = true;
            } else if (CAMPUS_COLEMAR_NAME.equals(campus.getName())) {
                campi.add(campus);
                hasColemar = true;
            }
        }

        if(!hasSamambaia) initCampusSamambaia();
        if(!hasColemar) initCampusColemar();
    }

    private void initCampusSamambaia(){
        Campus campusSamambaia = new Campus(CAMPUS_SAMAMBAIA_NAME);
        campusSamambaia.save();
        campi.add(campusSamambaia);
        initCampus(campusSamambaia, new int[]{105, 270, 174, 263, 268, 269, 302});
    }

    private void initCampusColemar(){
        Campus campusColemar = new Campus(CAMPUS_COLEMAR_NAME);
        campusColemar.save();
        campi.add(campusColemar);

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
                    if(sbl.getCampus().getId().equals(campus.getId())){
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
}
