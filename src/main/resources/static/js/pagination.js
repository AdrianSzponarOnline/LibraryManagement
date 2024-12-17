document.addEventListener("DOMContentLoaded", function () {
    function loadPage(page) {
        fetch(`/authors?page=${page}`)
            .then(response => response.text())
            .then(data => {
                const parser = new DOMParser();
                const newDocument = parser.parseFromString(data, 'text/html');
                const newAuthors = newDocument.querySelector("#authors");
                document.querySelector("#authors-container").innerHTML = newAuthors.outerHTML;

                const newPagination = newDocument.querySelector(".pagination");
                document.querySelector(".pagination").innerHTML = newPagination.innerHTML;

                addEventListeners();
            });
    }

    function addEventListeners() {
        document.querySelectorAll(".page-button, #prev-page, #next-page").forEach(button => {
            button.addEventListener("click", function () {
                const page = this.getAttribute("data-page");
                loadPage(page);
            });
        });
    }

    addEventListeners();
});
