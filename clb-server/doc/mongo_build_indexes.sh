db.fs.files.dropIndex("_filename");
db.clb_pdf.files.dropIndex("_filename");
db.clb_image.files.dropIndex("_filename");
db.clb_trivial.files.dropIndex("_filename");

db.fs.files.dropIndex("filename_1_uploadDate_1");
db.clb_image.files.dropIndex("filename_1_uploadDate_1");
db.clb_pdf.files.dropIndex("filename_1_uploadDate_1");
db.clb_trivial.files.dropIndex("filename_1_uploadDate_1");

db.clb_pdf.files.ensureIndex({"storageKey":1});
db.fs.files.ensureIndex({"storageKey":1});
db.clb_image.ensureIndex({"storageKey":1});
db.clb_trivial.files.ensureIndex({"storageKey":1});
db.clb_trivial.files.ensureIndex({"spaceName":1,"filename":1});
