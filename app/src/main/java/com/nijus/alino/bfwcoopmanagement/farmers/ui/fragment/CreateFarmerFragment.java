package com.nijus.alino.bfwcoopmanagement.farmers.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.farmers.adapter.CreateFarmerStepper;
import com.nijus.alino.bfwcoopmanagement.events.DataValidEventB;
import com.nijus.alino.bfwcoopmanagement.events.DataValidEventR;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.farmers.sync.AddFarmer;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.AbstractWizardModel;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.ModelCallbacks;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.BankInformation;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.BaseLine;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.Demographic;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.Finance;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.Forecast;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.General;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.ServiceAccess;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui.PageFragmentCallbacks;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui.StepPagerStrip;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.ProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateFarmerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateFarmerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateFarmerFragment extends Fragment implements ModelCallbacks,
        View.OnClickListener, ViewPager.OnPageChangeListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    public String LOG_TAG = CreateFarmerFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    private AbstractWizardModel mFarmerWizard;
    private Bundle saveBundle = new Bundle();
    private ProgressDialog progressDialog = new ProgressDialog();

    private Button mNextButton;
    private Button mPrevButton;
    private Button mSaveFarmer;
    private ImageView mImageView;
    private ImageView mRightImageView;

    private List<Page> mCurrentPageSequence;
    private StepPagerStrip mStepPagerStrip;
    private CreateFarmerStepper mPageAdapter;
    private PageFragmentCallbacks mCallbacks;
    private ViewPager pager;

    public CreateFarmerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mFarmerWizard = mCallbacks.onGetFarmerWizard();

        if (savedInstanceState != null) {
            mFarmerWizard.load(savedInstanceState.getBundle("model"));
        }

        mFarmerWizard.registerListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.farmer_wizard, container, false);
        pager = rootView.findViewById(R.id.pager_create_farmer);

        mCurrentPageSequence = mFarmerWizard.getCurrentPageSequence();

        mPageAdapter = new CreateFarmerStepper(getActivity().getSupportFragmentManager(),
                getActivity(), mCurrentPageSequence);
        if (pager != null) pager.setAdapter(mPageAdapter);

        mStepPagerStrip = rootView.findViewById(R.id.strip);

        pager.addOnPageChangeListener(this);

        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        mStepPagerStrip.setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
            @Override
            public void onPageStripSelected(int position) {
                position = Math.min(mPageAdapter.getCount() - 1, position);
                if (pager.getCurrentItem() != position) {
                    pager.setCurrentItem(position);
                }
            }
        });

        mNextButton = rootView.findViewById(R.id.next_button);
        mPrevButton = rootView.findViewById(R.id.prev_button);
        mSaveFarmer = rootView.findViewById(R.id.save_button);
        mImageView = rootView.findViewById(R.id.left_icon);
        mImageView.setImageResource(R.drawable.ic_chevron_left_black_24dp);
        mRightImageView = rootView.findViewById(R.id.right_icon);
        mRightImageView.setImageResource(R.drawable.ic_chevron_right_black_24dp);

        mNextButton.setOnClickListener(this);
        mPrevButton.setOnClickListener(this);
        mSaveFarmer.setOnClickListener(this);

        onPageTreeChanged();
        updateBottomBar();
        return rootView;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mStepPagerStrip.setCurrentPage(position);
        updateBottomBar();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.next_button) {
            if (pager.getCurrentItem() == mCurrentPageSequence.size() - 1) {
                //Save Farmer data
                saveNewFarmer();

            } else {
                if (pager.getCurrentItem() == 0) {
                    EventBus.getDefault().post(new DataValidEventB());
                } else {
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                }
            }
            updateBottomBar();
        } else if (view.getId() == R.id.prev_button) {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
            updateBottomBar();
        } else if (view.getId() == R.id.save_button) {
            //Save farmer data
            saveNewFarmer();
        }

    }

    public void saveNewFarmer() {
        //Get each page data
        //check if first page is filled if not hide progress dialog and show error message
        for (Page page : mCurrentPageSequence) {
            Bundle data = page.getData();

            boolean isData = data.containsKey("general");
            boolean isLandInfo = data.containsKey("land_info");
            boolean isForecast = data.containsKey("forecast");
            boolean isDemographic = data.containsKey("demographic");
            boolean isBaseline = data.containsKey("baseline");
            boolean isFinance = data.containsKey("finance");
            boolean isService = data.containsKey("serviceAccess");
            boolean isBank = data.containsKey("b_info");

            if (isData) {
                General general = data.getParcelable("general");
                if (general != null) {
                    if (general.getName() == null || general.getPhoneNumber() == null) {
                        Toast.makeText(getContext(), getResources().getString(R.string.data_valid_error), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                saveBundle.putParcelable("general", general);
            }

            if (isLandInfo) {
                HashMap map = (HashMap) data.getSerializable("land_info");
                saveBundle.putSerializable("land_info", map);
            }

            if (isForecast) {
                Forecast forecast = data.getParcelable("forecast");
                saveBundle.putParcelable("forecast", forecast);
            }
            if (isDemographic) {
                Demographic demographic = data.getParcelable("demographic");
                saveBundle.putParcelable("demographic", demographic);
            }
            if (isBaseline) {
                BaseLine baseLine = data.getParcelable("baseline");
                saveBundle.putParcelable("baseline", baseLine);
            }
            if (isFinance) {
                Finance finance = data.getParcelable("finance");
                saveBundle.putParcelable("finance", finance);
            }
            if (isService) {
                ServiceAccess serviceAccess = data.getParcelable("serviceAccess");
                saveBundle.putParcelable("serviceAccess", serviceAccess);
            }
            if (isBank) {
                BankInformation bankInformation = data.getParcelable("b_info");
                saveBundle.putParcelable("b_info", bankInformation);
            }
        }
        Intent intent = new Intent(getContext(), AddFarmer.class);
        intent.putExtra("farmer_data", saveBundle);
        progressDialog.show(getFragmentManager(), "farmerTag");
        getContext().startService(intent);
    }

    @Subscribe
    public void onDataValidEventR(DataValidEventR eventR) {
        if (eventR.isDataValid()) {
            pager.setCurrentItem(pager.getCurrentItem() + 1);
        }
    }

    @Subscribe
    public void onSaveDataEvent(SaveDataEvent saveDataEvent) {
        progressDialog.dismiss();
        getActivity().finish();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateVendorFragment.
     */
    public static CreateFarmerFragment newInstance(String param1, String param2) {
        CreateFarmerFragment fragment = new CreateFarmerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPageDataChanged(Page page) {
        if (page.isRequired()) {
            if (recalculateCutOffPage()) {
                mPageAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }

    @Override
    public void onPageTreeChanged() {
        mCurrentPageSequence = mFarmerWizard.getCurrentPageSequence();
        recalculateCutOffPage();
        mStepPagerStrip.setPageCount(mCurrentPageSequence.size());
        mPageAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", mFarmerWizard.save());
    }

    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = mCurrentPageSequence.size();
        for (int i = 0; i < mCurrentPageSequence.size(); i++) {
            Page page = mCurrentPageSequence.get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }

        if (mPageAdapter.getCutOffPage() != cutOffPage) {
            mPageAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void updateBottomBar() {
        int position = pager.getCurrentItem();
        if (position == mCurrentPageSequence.size() - 1) {
            mNextButton.setText(R.string.finish);
        } else {
            mNextButton.setText(R.string.next);
            TypedValue v = new TypedValue();
            getActivity().getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v, true);
            mNextButton.setEnabled(position != mPageAdapter.getCutOffPage());
        }

        mSaveFarmer.setVisibility(position > 0 && position < mCurrentPageSequence.size() - 1 ? View.VISIBLE : View.INVISIBLE);

        mPrevButton.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
        mImageView.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFarmerWizard.unregisterListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mCallbacks = (PageFragmentCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
