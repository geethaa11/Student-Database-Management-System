const API_BASE = '/api/students';

// Navigation
function showView(viewId) {
    document.getElementById('dashboard-view').style.display = viewId === 'dashboard' ? 'block' : 'none';
    document.getElementById('students-view').style.display = viewId === 'students' ? 'block' : 'none';
    if(viewId === 'dashboard') loadStats();
}

// Stats
async function loadStats() {
    try {
        const res = await fetch(`${API_BASE}/stats`);
        const data = await res.json();
        
        document.getElementById('stat-total-students').innerText = data.totalStudents || 0;
        document.getElementById('stat-total-depts').innerText = data.totalDepartments || 0;
        
        const distContainer = document.getElementById('stat-dist-container');
        distContainer.innerHTML = '';
        for (const [dept, count] of Object.entries(data.distribution || {})) {
            const div = document.createElement('div');
            div.className = 'dist-label';
            div.innerText = `${dept}: ${count}`;
            distContainer.appendChild(div);
        }
    } catch (e) {
        console.error('Failed to load stats', e);
    }
}

// Students CRUD
let currentStudents = [];
let selectedId = null;

async function loadStudents(query = '') {
    try {
        const url = query ? `${API_BASE}?query=${encodeURIComponent(query)}` : API_BASE;
        const res = await fetch(url);
        currentStudents = await res.json();
        renderTable();
    } catch (e) {
        console.error('Failed to load students', e);
    }
}

function renderTable() {
    const tbody = document.getElementById('student-table-body');
    tbody.innerHTML = '';
    currentStudents.forEach(s => {
        const tr = document.createElement('tr');
        if (s.id === selectedId) tr.classList.add('selected');
        
        tr.innerHTML = `
            <td>${s.rollNumber}</td>
            <td>${s.fullName}</td>
            <td>${s.department}</td>
            <td>${s.cgpa || ''}</td>
        `;
        tr.onclick = () => selectStudent(s.id);
        tbody.appendChild(tr);
    });
}

function selectStudent(id) {
    selectedId = id;
    renderTable(); // Update selection highlight
    
    const student = currentStudents.find(s => s.id === id);
    if (!student) return;

    document.getElementById('studentId').value = student.id;
    document.getElementById('rollNumber').value = student.rollNumber || '';
    document.getElementById('fullName').value = student.fullName || '';
    document.getElementById('dob').value = student.dob || '';
    document.getElementById('gender').value = student.gender || '';
    document.getElementById('email').value = student.email || '';
    document.getElementById('phone').value = student.phone || '';
    document.getElementById('department').value = student.department || '';
    document.getElementById('yearOfStudy').value = student.yearOfStudy || '';
    document.getElementById('cgpa').value = student.cgpa || '';
    document.getElementById('address').value = student.address || '';
    
    setStatus('');
}

function clearForm() {
    selectedId = null;
    document.getElementById('student-form').reset();
    document.getElementById('studentId').value = '';
    renderTable(); // clear selection highlight
    setStatus('');
}

async function saveStudent(e) {
    e.preventDefault();
    
    const id = document.getElementById('studentId').value;
    const payload = {
        rollNumber: document.getElementById('rollNumber').value,
        fullName: document.getElementById('fullName').value,
        dob: document.getElementById('dob').value,
        gender: document.getElementById('gender').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('phone').value,
        department: document.getElementById('department').value,
        yearOfStudy: parseInt(document.getElementById('yearOfStudy').value) || null,
        cgpa: parseFloat(document.getElementById('cgpa').value) || null,
        address: document.getElementById('address').value
    };

    try {
        const method = id ? 'PUT' : 'POST';
        const url = id ? `${API_BASE}/${id}` : API_BASE;
        
        const res = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        
        if (res.ok) {
            setStatus('Student saved successfully!', 'success');
            clearForm();
            loadStudents();
        } else {
            const err = await res.json();
            setStatus(err.error || err.details || 'Failed to save', 'danger');
        }
    } catch (err) {
        setStatus('Network error', 'danger');
    }
}

async function deleteStudent() {
    if (!selectedId) {
        setStatus('Please select a student to delete', 'danger');
        return;
    }
    
    if (!confirm('Are you sure you want to delete this student?')) return;
    
    try {
        const res = await fetch(`${API_BASE}/${selectedId}`, { method: 'DELETE' });
        if (res.ok) {
            setStatus('Student deleted successfully!', 'success');
            clearForm();
            loadStudents();
        } else {
            setStatus('Failed to delete student', 'danger');
        }
    } catch (e) {
        setStatus('Network error', 'danger');
    }
}

function searchStudents() {
    const query = document.getElementById('searchInput').value;
    loadStudents(query);
}

function setStatus(msg, type = '') {
    const el = document.getElementById('form-status');
    el.innerText = msg;
    el.className = 'status-msg ' + (type ? `text-${type}` : '');
}

// Initial load
window.onload = () => {
    showView('dashboard');
};
