function DOMtoString(document_root) {
  var html = '';
  if (document_root.location.href.includes("emag.ro") &&
  (document_root.location.href.match(/\//g) || []).length == 6) {
    var titles = document_root.getElementsByClassName('page-title');
    for (var i =0 ; i < titles.length; i++) {
       html = html + "\n" + titles[i].innerHTML;
    }
  } else if (document_root.location.href.includes("flanco.ro") &&
    (document_root.location.href.match(/\//g) || []).length == 3
  ) {
    var titles = document_root.querySelector('span[data-ui-id=page-title-wrapper]');
    html = html + "\n" +titles.innerHTML;
  } else if (document_root.location.href.includes("altex.ro") &&
    (document_root.location.href.match(/\//g) || []).length == 6) {
    console.log( document_root.getElementsByClassName('mt-2')[0].innerHTML);
      var titles = document_root.getElementsByClassName('lg:text-3xl')[0].getElementsByTagName("div")[0].innerHTML;
          html = html + "\n" + titles;
    } else {
    html = "Unsupported site";
    return html;
    }

    var xhr = new XMLHttpRequest();
    xhr.open("POST", 'http://localhost:8080/api/server', false);
    var body = {
    "html": html,
    "site": document_root.location.href
    };
    //Send the proper header information along with the request
    xhr.setRequestHeader("Content-Type", "application/json");
    console.log(body);
    xhr.send(JSON.stringify(body));
    return xhr.responseText;
}

chrome.runtime.sendMessage({
    action: "getSource",
    source: DOMtoString(document)
});