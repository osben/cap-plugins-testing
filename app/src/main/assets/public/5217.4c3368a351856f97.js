"use strict";(self.webpackChunkapp=self.webpackChunkapp||[]).push([[5217],{5217:(_,D,E)=>{E.r(D),E.d(D,{FilesystemWeb:()=>S});var l=E(1670),P=E(6549),w=(()=>((w=w||{}).UTF8="utf8",w.ASCII="ascii",w.UTF16="utf16",w))();function F(p){const v=p.split("/").filter(e=>"."!==e),n=[];return v.forEach(e=>{".."===e&&n.length>0&&".."!==n[n.length-1]?n.pop():n.push(e)}),n.join("/")}let S=(()=>{class p extends P.Uw{constructor(){var n;super(...arguments),n=this,this.DB_VERSION=1,this.DB_NAME="Disc",this._writeCmds=["add","put","delete"],this.downloadFile=function(){var e=(0,l.Z)(function*(t){const r=(0,P.Sd)(t,t.webFetchExtra),i=yield fetch(t.url,r);let o;if(null!=t&&t.progress)if(null!=i&&i.body){const s=i.body.getReader();let a=0;const u=[],y=i.headers.get("content-type"),b=parseInt(i.headers.get("content-length")||"0",10);for(;;){const{done:f,value:m}=yield s.read();if(f)break;u.push(m),a+=(null==m?void 0:m.length)||0,n.notifyListeners("progress",{url:t.url,bytes:a,contentLength:b})}const g=new Uint8Array(a);let h=0;for(const f of u)typeof f>"u"||(g.set(f,h),h+=f.length);o=new Blob([g.buffer],{type:y||void 0})}else o=new Blob;else o=yield i.blob();const d=URL.createObjectURL(o),c=document.createElement("a");return document.body.appendChild(c),c.href=d,c.download=t.path,c.click(),URL.revokeObjectURL(d),document.body.removeChild(c),{path:t.path,blob:o}});return function(t){return e.apply(this,arguments)}}()}initDb(){var n=this;return(0,l.Z)(function*(){if(void 0!==n._db)return n._db;if(!("indexedDB"in window))throw n.unavailable("This browser doesn't support IndexedDB");return new Promise((e,t)=>{const r=indexedDB.open(n.DB_NAME,n.DB_VERSION);r.onupgradeneeded=p.doUpgrade,r.onsuccess=()=>{n._db=r.result,e(r.result)},r.onerror=()=>t(r.error),r.onblocked=()=>{console.warn("db blocked")}})})()}static doUpgrade(n){const t=n.target.result;t.objectStoreNames.contains("FileStorage")&&t.deleteObjectStore("FileStorage"),t.createObjectStore("FileStorage",{keyPath:"path"}).createIndex("by_folder","folder")}dbRequest(n,e){var t=this;return(0,l.Z)(function*(){const r=-1!==t._writeCmds.indexOf(n)?"readwrite":"readonly";return t.initDb().then(i=>new Promise((o,d)=>{const a=i.transaction(["FileStorage"],r).objectStore("FileStorage")[n](...e);a.onsuccess=()=>o(a.result),a.onerror=()=>d(a.error)}))})()}dbIndexRequest(n,e,t){var r=this;return(0,l.Z)(function*(){const i=-1!==r._writeCmds.indexOf(e)?"readwrite":"readonly";return r.initDb().then(o=>new Promise((d,c)=>{const y=o.transaction(["FileStorage"],i).objectStore("FileStorage").index(n)[e](...t);y.onsuccess=()=>d(y.result),y.onerror=()=>c(y.error)}))})()}getPath(n,e){const t=void 0!==e?e.replace(/^[/]+|[/]+$/g,""):"";let r="";return void 0!==n&&(r+="/"+n),""!==e&&(r+="/"+t),r}clear(){var n=this;return(0,l.Z)(function*(){(yield n.initDb()).transaction(["FileStorage"],"readwrite").objectStore("FileStorage").clear()})()}readFile(n){var e=this;return(0,l.Z)(function*(){const t=e.getPath(n.directory,n.path),r=yield e.dbRequest("get",[t]);if(void 0===r)throw Error("File does not exist.");return{data:r.content?r.content:""}})()}writeFile(n){var e=this;return(0,l.Z)(function*(){const t=e.getPath(n.directory,n.path);let r=n.data;const i=n.encoding,o=n.recursive,d=yield e.dbRequest("get",[t]);if(d&&"directory"===d.type)throw Error("The supplied path is a directory.");const c=t.substr(0,t.lastIndexOf("/"));if(void 0===(yield e.dbRequest("get",[c]))){const y=c.indexOf("/",1);if(-1!==y){const b=c.substr(y);yield e.mkdir({path:b,directory:n.directory,recursive:o})}}if(!i&&(r=r.indexOf(",")>=0?r.split(",")[1]:r,!e.isBase64String(r)))throw Error("The supplied data is not valid base64 content.");const a=Date.now(),u={path:t,folder:c,type:"file",size:r.length,ctime:a,mtime:a,content:r};return yield e.dbRequest("put",[u]),{uri:u.path}})()}appendFile(n){var e=this;return(0,l.Z)(function*(){const t=e.getPath(n.directory,n.path);let r=n.data;const i=n.encoding,o=t.substr(0,t.lastIndexOf("/")),d=Date.now();let c=d;const s=yield e.dbRequest("get",[t]);if(s&&"directory"===s.type)throw Error("The supplied path is a directory.");if(void 0===(yield e.dbRequest("get",[o]))){const y=o.indexOf("/",1);if(-1!==y){const b=o.substr(y);yield e.mkdir({path:b,directory:n.directory,recursive:!0})}}if(!i&&!e.isBase64String(r))throw Error("The supplied data is not valid base64 content.");void 0!==s&&(r=void 0===s.content||i?s.content+r:btoa(atob(s.content)+atob(r)),c=s.ctime);const u={path:t,folder:o,type:"file",size:r.length,ctime:c,mtime:d,content:r};yield e.dbRequest("put",[u])})()}deleteFile(n){var e=this;return(0,l.Z)(function*(){const t=e.getPath(n.directory,n.path);if(void 0===(yield e.dbRequest("get",[t])))throw Error("File does not exist.");if(0!==(yield e.dbIndexRequest("by_folder","getAllKeys",[IDBKeyRange.only(t)])).length)throw Error("Folder is not empty.");yield e.dbRequest("delete",[t])})()}mkdir(n){var e=this;return(0,l.Z)(function*(){const t=e.getPath(n.directory,n.path),r=n.recursive,i=t.substr(0,t.lastIndexOf("/")),o=(t.match(/\//g)||[]).length,d=yield e.dbRequest("get",[i]),c=yield e.dbRequest("get",[t]);if(1===o)throw Error("Cannot create Root directory");if(void 0!==c)throw Error("Current directory does already exist.");if(!r&&2!==o&&void 0===d)throw Error("Parent directory must exist");if(r&&2!==o&&void 0===d){const u=i.substr(i.indexOf("/",1));yield e.mkdir({path:u,directory:n.directory,recursive:r})}const s=Date.now(),a={path:t,folder:i,type:"directory",size:0,ctime:s,mtime:s};yield e.dbRequest("put",[a])})()}rmdir(n){var e=this;return(0,l.Z)(function*(){const{path:t,directory:r,recursive:i}=n,o=e.getPath(r,t),d=yield e.dbRequest("get",[o]);if(void 0===d)throw Error("Folder does not exist.");if("directory"!==d.type)throw Error("Requested path is not a directory");const c=yield e.readdir({path:t,directory:r});if(0!==c.files.length&&!i)throw Error("Folder is not empty");for(const s of c.files){const a=`${t}/${s.name}`;"file"===(yield e.stat({path:a,directory:r})).type?yield e.deleteFile({path:a,directory:r}):yield e.rmdir({path:a,directory:r,recursive:i})}yield e.dbRequest("delete",[o])})()}readdir(n){var e=this;return(0,l.Z)(function*(){const t=e.getPath(n.directory,n.path),r=yield e.dbRequest("get",[t]);if(""!==n.path&&void 0===r)throw Error("Folder does not exist.");const i=yield e.dbIndexRequest("by_folder","getAllKeys",[IDBKeyRange.only(t)]);return{files:yield Promise.all(i.map(function(){var d=(0,l.Z)(function*(c){let s=yield e.dbRequest("get",[c]);return void 0===s&&(s=yield e.dbRequest("get",[c+"/"])),{name:c.substring(t.length+1),type:s.type,size:s.size,ctime:s.ctime,mtime:s.mtime,uri:s.path}});return function(c){return d.apply(this,arguments)}}()))}})()}getUri(n){var e=this;return(0,l.Z)(function*(){const t=e.getPath(n.directory,n.path);let r=yield e.dbRequest("get",[t]);return void 0===r&&(r=yield e.dbRequest("get",[t+"/"])),{uri:(null==r?void 0:r.path)||t}})()}stat(n){var e=this;return(0,l.Z)(function*(){const t=e.getPath(n.directory,n.path);let r=yield e.dbRequest("get",[t]);if(void 0===r&&(r=yield e.dbRequest("get",[t+"/"])),void 0===r)throw Error("Entry does not exist.");return{type:r.type,size:r.size,ctime:r.ctime,mtime:r.mtime,uri:r.path}})()}rename(n){var e=this;return(0,l.Z)(function*(){yield e._copy(n,!0)})()}copy(n){var e=this;return(0,l.Z)(function*(){return e._copy(n,!1)})()}requestPermissions(){return(0,l.Z)(function*(){return{publicStorage:"granted"}})()}checkPermissions(){return(0,l.Z)(function*(){return{publicStorage:"granted"}})()}_copy(n,e=!1){var t=this;return(0,l.Z)(function*(){let{toDirectory:r}=n;const{to:i,from:o,directory:d}=n;if(!i||!o)throw Error("Both to and from must be provided");r||(r=d);const c=t.getPath(d,o),s=t.getPath(r,i);if(c===s)return{uri:s};if(function q(p,v){p=F(p),v=F(v);const n=p.split("/"),e=v.split("/");return p!==v&&n.every((t,r)=>t===e[r])}(c,s))throw Error("To path cannot contain the from path");let a;try{a=yield t.stat({path:i,directory:r})}catch{const h=i.split("/");h.pop();const f=h.join("/");if(h.length>0&&"directory"!==(yield t.stat({path:f,directory:r})).type)throw new Error("Parent directory of the to path is a file")}if(a&&"directory"===a.type)throw new Error("Cannot overwrite a directory with a file");const u=yield t.stat({path:o,directory:d}),y=function(){var g=(0,l.Z)(function*(h,f,m){const x=t.getPath(r,h),R=yield t.dbRequest("get",[x]);R.ctime=f,R.mtime=m,yield t.dbRequest("put",[R])});return function(f,m,x){return g.apply(this,arguments)}}(),b=u.ctime?u.ctime:Date.now();switch(u.type){case"file":{const g=yield t.readFile({path:o,directory:d});let h;e&&(yield t.deleteFile({path:o,directory:d})),t.isBase64String(g.data)||(h=w.UTF8);const f=yield t.writeFile({path:i,directory:r,data:g.data,encoding:h});return e&&(yield y(i,b,u.mtime)),f}case"directory":{if(a)throw Error("Cannot move a directory over an existing object");try{yield t.mkdir({path:i,directory:r,recursive:!1}),e&&(yield y(i,b,u.mtime))}catch{}const g=(yield t.readdir({path:o,directory:d})).files;for(const h of g)yield t._copy({from:`${o}/${h.name}`,to:`${i}/${h.name}`,directory:d,toDirectory:r},e);e&&(yield t.rmdir({path:o,directory:d}))}}return{uri:s}})()}isBase64String(n){try{return btoa(atob(n))==n}catch{return!1}}}return p._debug=!0,p})()}}]);