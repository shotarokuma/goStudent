package cmpt362.group14.gostudent.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cmpt362.group14.gostudent.ItemAdapter
import cmpt362.group14.gostudent.databinding.ActivityUserItemsBinding
import cmpt362.group14.gostudent.model.Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class UserItemsFragment : Fragment() {

    private lateinit var listener: ListenerRegistration
    private lateinit var db: FirebaseFirestore
    private var binding: ActivityUserItemsBinding? = null
    private var itemList = ArrayList<Item>()
    private lateinit var auth: FirebaseAuth
    private lateinit var uid: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityUserItemsBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        uid = auth.currentUser?.uid.toString()
        fetchItems()
        return binding!!.root
    }

    private fun fetchItems() {
        listener = db.collection("item")
            .whereEqualTo("sellerId", uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w(HomeChatFragment.TAG, "Listen failed.", error)
                    return@addSnapshotListener
                }

                for (dc in value!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            val item: Item = dc.document.toObject(Item::class.java)
                            itemList.add(item)
                            if (binding != null) {
                                binding!!.listviewItems.adapter =
                                    ItemAdapter(requireActivity(), itemList)
                            }
                        }
                        DocumentChange.Type.MODIFIED ->
                            {
                                val newItem: Item = dc.document.toObject(Item::class.java)
                                itemList.remove(itemList.find { item: Item -> item.iid == newItem.iid })
                                itemList.add(newItem)
                                if (binding != null) {
                                    binding!!.listviewItems.adapter =
                                        ItemAdapter(requireActivity(), itemList)
                                }
                            }
                        DocumentChange.Type.REMOVED -> {
                            val item: Item = dc.document.toObject(Item::class.java)
                            itemList.remove(item)
                            if (binding != null) {
                                binding!!.listviewItems.adapter =
                                    ItemAdapter(requireActivity(), itemList)
                            }
                        }
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        listener.remove()
    }
}
