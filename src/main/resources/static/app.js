async function jsonFetch(url, options = {}) {
  const headers = {'Content-Type': 'application/json', ...(options.headers||{})};
  const res = await fetch(url, {...options, headers});
  if (!res.ok) throw new Error(await res.text().catch(()=>res.statusText));
  return res.status === 204 ? null : res.json();
}
function escapeHtml(s){return (s??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));}

document.addEventListener('DOMContentLoaded', () => {
  const body = document.body;

  // /ui/books page
  if (location.pathname === '/ui/books') {
    const booksEndpoint = body.dataset.booksEndpoint || '/api/books';
    const listEl = document.getElementById('books-list');
    const form = document.getElementById('create-book-form');

    async function renderBooks() {
      const books = await jsonFetch(booksEndpoint);
      listEl.innerHTML = '';
      for (const b of books) {
        const name = b.label ?? `Book ${b.id}`;
        const li = document.createElement('li');
        li.innerHTML = `<a href="/ui/books/${b.id}">${escapeHtml(name)}</a>`;
        listEl.appendChild(li);
      }
    }

    if (form) {
      form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const label = form.querySelector('input[name="label"]')?.value || null;
        await jsonFetch(booksEndpoint, { method: 'POST', body: JSON.stringify({ label }) });
        await renderBooks();
        form.reset();
      });
    }

    renderBooks();
  }

  // /ui/books/{id} page
  if (/^\/ui\/books\/\d+$/.test(location.pathname)) {
    const bookId = body.dataset.bookId;
    const bookEndpoint = body.dataset.bookEndpoint || `/api/books/${bookId}`;
    const addBuddyEndpoint = body.dataset.addBuddyEndpoint || `/api/books/${bookId}/buddies`;
    const deleteTpl = body.dataset.deleteBuddyEndpointTemplate || '/api/buddies/{id}';

    const table = document.getElementById('buddies-table');
    const addForm = document.getElementById('add-buddy-form');

    async function renderBook() {
      const data = await jsonFetch(bookEndpoint);
      document.querySelector('h1').textContent = data.label ?? `Address Book ${data.id}`;
      const header = table.querySelector('tr:first-child').outerHTML;
      const rows = (data.buddies || []).map(b => `
        <tr>
          <td>${escapeHtml(b.name||'')}</td>
          <td>${escapeHtml(b.phoneNumber||'')}</td>
          <td>${escapeHtml(b.email||'')}</td>
          <td>
            <form action="/buddies/${b.id}/delete" method="post" data-buddy-id="${b.id}">
              <input type="hidden" name="bookId" value="${data.id}"/>
              <button>Delete</button>
            </form>
          </td>
        </tr>`).join('');
      table.innerHTML = header + rows;
    }

    if (addForm) {
      addForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const fd = new FormData(addForm);
        const payload = {
          name: fd.get('name'),
          phoneNumber: fd.get('phoneNumber'),
          email: fd.get('email')
        };
        await jsonFetch(addBuddyEndpoint, { method: 'POST', body: JSON.stringify(payload) });
        await renderBook();
        addForm.reset();
      });
    }

    document.body.addEventListener('click', async (e) => {
      const form = e.target.closest('form[action*="/buddies/"][method="post"]');
      if (!form) return;
      e.preventDefault();
      const id = form.dataset.buddyId || (form.action.match(/\/buddies\/(\d+)\//)||[])[1];
      if (!id) return;
      await jsonFetch(deleteTpl.replace('{id}', id), { method: 'DELETE' });
      await renderBook();
    });

    renderBook();
  }
});
