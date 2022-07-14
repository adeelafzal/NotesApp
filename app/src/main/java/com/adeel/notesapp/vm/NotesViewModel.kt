package com.adeel.notesapp.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adeel.notesapp.model.NoteRequest
import com.adeel.notesapp.model.NoteResponse
import com.adeel.notesapp.repo.NotesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val notesRepo: NotesRepo) : ViewModel() {

    val notesLiveData get() = notesRepo.notesLiveData
    val statusLiveData get() = notesRepo.statusLiveData
    fun createNote(noteRequest: NoteRequest) {
        viewModelScope.launch {
            notesRepo.addNotes(noteRequest)
        }
    }

    fun getAllNotes() {
        viewModelScope.launch {
            notesRepo.getNotes()
        }
    }

    fun updateNote(id: String, noteRequest: NoteRequest) {
        viewModelScope.launch {
            notesRepo.updateNote(id, noteRequest)
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            notesRepo.deleteNote(noteId)
        }
    }

}