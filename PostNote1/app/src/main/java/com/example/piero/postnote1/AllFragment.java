package com.example.piero.postnote1;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AllFragment extends Fragment implements SearchView.OnQueryTextListener {

    public static AllFragment newInstance() {return new AllFragment();};

    private static ArrayList<PostItem> allList = new ArrayList<>();
    private static List<PostItem> filteredModelList = new ArrayList<>();
    private static RecyclerView recyclerView;
    public static PostAdapter mAdapter;
    private String[] scelte = {"Share", "Delete"};
    private final Integer[] icons = new Integer[] {R.drawable.ic_menu_share,R.drawable.ic_delete_forever};
    DatabaseHelper myDB;
    public String TAGCICLO = "CICLODIVITA";
    private String myID = "";
    Dettaglio dettaglio;
    private Paint p = new Paint();
    private View view;


    public AllFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAGCICLO, "On Attach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAGCICLO, "On Create");
        setHasOptionsMenu(true);
    }

    public static AllFragment getIstance(){
        return new AllFragment();
    }


    private static final String TAG = "RecyclerViewFragment";

    /*public void onPrepareOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(getActivity().getApplicationContext());

        menu.clear();

        super.onPrepareOptionsMenu(menu);
        inflater.inflate(R.menu.main, menu);
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        Log.d("MAFUNZIONA", "Pare di sì");


        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 //        Bundle bundle = data.getExtras();
        if(resultCode == -1){
            //PostItem post = (PostItem)bundle.getSerializable("POST");
            //int id = bundle.getInt("ID");
            //Log.d("MAIN" , " " +id );
            Log.d("PRIMA ", allList.toString());
            //addToList(post, id);
            Log.d("Dopo ", allList.toString());
            //Log.d("MAIN", " " + id);
            UpdateList();
        }
        if(resultCode == 99){
            //deleteElement(bundle.getInt("ID"));
            UpdateList();
        }

    }

    public void deleteElement(int id){
        if(id != allList.size())
            allList.remove(id);
    }


    public void addToList(PostItem postItem, int position){

        if(!allList.isEmpty()){
            if(position < allList.size()){
                allList.set(position, postItem);
            }else {
                allList.add(postItem);
            }
        }else {
            allList.add(postItem);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("ALLLIST", allList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all, container, false);
        rootView.setTag(TAG);

        myDB = new DatabaseHelper(getActivity());
        dettaglio = new Dettaglio();

        Log.d(TAGCICLO, "onCreateView");
        if (allList.isEmpty()){
            allList = getArguments().getParcelableArrayList("postList");
        }

        if(savedInstanceState != null)
            allList = (ArrayList<PostItem>) savedInstanceState.getSerializable("ALLLIST");
        allList = getArguments().getParcelableArrayList("postList");

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);

        mAdapter = new PostAdapter(allList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        //mLayoutManager.scrollToPosition(allList.size()-1);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        //recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(25));
        recyclerView.setAdapter(mAdapter);

        initSwipe();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.d("TAG", "" + position);
                PostItem item = filteredModelList.get(position);
                Intent i = new Intent(getActivity(), Dettaglio.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("MyPost", item);
                bundle.putInt("ID", allList.get(position).getId());
                //startActivity(i.putExtras(bundle));
                startActivityForResult(i.putExtras(bundle), 10);
            }

            @Override
            public void onLongClick(View view, final int position) {/*
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                ListAdapter adapter = new ArrayAdapterWithIcon(getActivity(), scelte, icons);
                builder.setTitle("Cosa Desideri fare?")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0: {
                                        Intent sendIntent = new Intent();
                                        PostItem temPostItem = filteredModelList.get(position);
                                        sendIntent.setAction(Intent.ACTION_SEND);
                                        sendIntent.putExtra(Intent.EXTRA_TEXT, temPostItem.getTitolo().toUpperCase() + "\n"
                                                + temPostItem.getTesto());
                                        sendIntent.setType("text/*");
                                        Log.d("primo", "primo");
                                        if (temPostItem.getPosizioneAudio() != null) {
                                            sendIntent.setType("audio/*");
                                            sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(temPostItem.getPosizioneAudio())));
                                            Log.d("secondo", "secondo");
                                        }
                                        if (temPostItem.getcreationDate() != null && temPostItem.getcreationDate() != "") {
                                            Uri pictureUri = Uri.parse(temPostItem.getcreationDate().replaceAll("/", "").replaceAll(":", "").replaceAll(" ", "").toString());
                                            sendIntent.setType("image/*");
                                            final File photoFile = new File(temPostItem.getcreationDate().replaceAll("/", "").replaceAll(":", "").replaceAll(" ", "").toString());
                                            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                            sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));

                                            Log.d("terzo", "terzo");
                                        }

                                        Log.d("fine", "fine");
                                        startActivity(Intent.createChooser(sendIntent, "Scegli"));
                                        break;

                                    }
                                    case 1: {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setCancelable(true);
                                        builder.setTitle("Attenzione");
                                        builder.setMessage("Vuoi cancellare questa nota?");
                                        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Log.d("Position", String.valueOf(position));
                                                myID = String.valueOf(filteredModelList.get(position).getId());
                                                DeleteData(myID);
                                                allList.remove(position);
                                                filteredModelList = allList;
                                                UpdateList();
                                                Log.d("WEYY", "" + allList.size());
                                                Log.d("WEYY", "" + filteredModelList.size());
                                            }
                                        });
                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        builder.show();
                                        builder.create();
                                    }
                                }
                            }
                        });
                builder.create();
                builder.show();

            */}
        }));
        return rootView;
    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    myID = String.valueOf(filteredModelList.get(position).getId());
                    DeleteData(myID);
                    allList.remove(position);
                    filteredModelList = allList;
                    UpdateList();
                } else {
                    Intent sendIntent = new Intent();
                    PostItem temPostItem = filteredModelList.get(position);
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Titolo: " + temPostItem.getTitolo().toUpperCase() + "\n" + "Testo: " + temPostItem.getTesto());
                    sendIntent.setType("text/*");
                    String percorsoImg = Environment.getExternalStorageDirectory() + File.separator + "PostNoteImage" + File.separator + temPostItem.getcreationDate().replaceAll("/", "").replaceAll(":","").replaceAll(" ", "");
                    if(temPostItem.getImmagine() == 1 && percorsoImg != null){
                        Uri screenshotUri = Uri.parse(percorsoImg);
                        sendIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri
                                );
                        sendIntent.setType("image/*");
                    }
                    startActivity(Intent.createChooser(sendIntent, "Scegli"));

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_all_black_48dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_clear_black_24dp);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    private void removeView(){
        if(view.getParent()!=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAGCICLO, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAGCICLO, "onResume");
        Log.d("Hey, listen", "" + allList.size());
        filteredModelList = allList;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAGCICLO, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAGCICLO, "onStop");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAGCICLO, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAGCICLO, "onDetach");
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private AllFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final AllFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }


        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static void UpdateList() {
        recyclerView.scrollToPosition(allList.size() - 1);
        mAdapter.notifyDataSetChanged();
    }


    public void DeleteData(String mioID){
        Log.d("Position", mioID);
        Integer deleteRows = myDB.deleteData(mioID);
        if(deleteRows > 0){
            Toast.makeText(getActivity(), "Data Delete", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity(), "Data Not Delete", Toast.LENGTH_LONG).show();
        }
    }


    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.show();
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Log.d("MAFUNZIONA", query);
        filteredModelList = filter(allList, query);
        //allList = getArguments().getParcelableArrayList("postList");
        //mAdapter.animateTo(filteredModelList);
        mAdapter.setPostList(filteredModelList);

        recyclerView.scrollToPosition(allList.size()-1);
        mAdapter.notifyDataSetChanged();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) { return false; }

    private List<PostItem> filter(List<PostItem> models, String query) {
        query = query.toLowerCase();

        final List<PostItem> filteredModelList = new ArrayList<>();
        for (PostItem model : models) {
            final String text = model.getTitolo().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

}
