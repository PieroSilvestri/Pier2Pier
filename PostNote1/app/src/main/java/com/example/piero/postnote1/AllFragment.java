package com.example.piero.postnote1;


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
import android.support.v7.app.AlertDialog;
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
    private String[] scelte = {"Favourite"};
    private final Integer[] icons = new Integer[] {R.drawable.ic_flag};
    DatabaseHelper myDB;
    public String TAGCICLO = "CICLODIVITA";
    private String myID = "";
    Dettaglio dettaglio;
    private Paint p = new Paint();
    private View view;
    private static String query;


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
        query = "";
    }

    public static AllFragment getIstance(){
        return new AllFragment();
    }


    private static final String TAG = "RecyclerViewFragment";

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        Log.d("MAFUNZIONA", "Pare di sì");


        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == -1){
            Log.d("PRIMA ", allList.toString());
            Log.d("Dopo ", allList.toString());

            UpdateList();
        }
        if(resultCode == 99){
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

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(25));
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
                bundle.putInt("AUDIO", allList.get(position).getAudio());
                startActivityForResult(i.putExtras(bundle), 10);
            }

            @Override
            public void onLongClick(View view, final int position) {
                PostItem temPostItem = filteredModelList.get(position);
                if (temPostItem.isFlagged() == 0) {
                    temPostItem.setFlagged(1);
                    String id = String.valueOf(temPostItem.getId());
                    boolean isFlagged = myDB.updateFlag(id, 1);
                    if(isFlagged){
                        Toast.makeText(getActivity(), "Nota aggiunta ai preferiti",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(), "ERRORE UPDATE",Toast.LENGTH_LONG).show();
                    }
                } else {
                    temPostItem.setFlagged(0);
                    String id = String.valueOf(temPostItem.getId());
                    boolean isFlagged = myDB.updateFlag(id, 0);
                    if(isFlagged){
                        Toast.makeText(getActivity(), "Nota rimossa dai preferiti",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(), "ERRORE UPDATE",Toast.LENGTH_LONG).show();
                    }                }
                AllFragment.UpdateList();

            }
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
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(true);
                    builder.setTitle("ATTENZIONE ATTENZIONE");
                    builder.setMessage("Sicuro di voler cancellare la nota?");
                    builder.setPositiveButton("Sì", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            PostItem removePost = allList.get(position);
                            allList.remove(allList.indexOf(removePost));

                            String selectedFilePath = (Environment.getExternalStorageDirectory() + File.separator + "PostNoteImage" + File.separator + removePost.getcreationDate().replaceAll("/", "").replaceAll(":","").replaceAll(" ", "") + ".jpg");
                            File file = new File(selectedFilePath);
                            if(file.exists())
                                file.delete();
                            String selectedFilePathAudio = (Environment.getExternalStorageDirectory() + File.separator + "PostNoteAudio" + File.separator +  "audioRecord" + removePost.getcreationDate().replaceAll("/", "").replaceAll(":","").replaceAll(" ", "") + ".mp3");
                            File fileAudio = new File(selectedFilePathAudio);
                            if(fileAudio.exists())
                                fileAudio.delete();

                            DeleteData(String.valueOf(removePost.getId()));

                            Log.d("ANOTHER ONE", "" + removePost.getTitolo());
                            filteredModelList = allList;
                            UpdateList();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            UpdateList();
                        }

                    });
                    builder.show();


                } else {
                    Intent sendIntent = new Intent();
                    PostItem temPostItem = filteredModelList.get(position);
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Titolo: " + temPostItem.getTitolo().toUpperCase() + "\n" + "Testo: " + temPostItem.getTesto());
                    sendIntent.setType("text/*");
                    String percorsoImg = Environment.getExternalStorageDirectory() + File.separator + "PostNoteImage" + File.separator + temPostItem.getcreationDate().replaceAll("/", "").replaceAll(":","").replaceAll(" ", "") + ".jpg";
                    File img = new File(percorsoImg);
                    if(temPostItem.getImmagine() == 1 && img.exists()){
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
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(),itemView.getRight()+ dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_forever);
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
