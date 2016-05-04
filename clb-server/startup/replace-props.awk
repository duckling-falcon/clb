function trim(str)
{
        sub("^[ \t]*", "", str);
        sub("[ \t]*$", "", str);
        return str
}
BEGIN   {
        FS="=";
}
FILENAME==fp"/setup.properties" && $0 !~ /#|^$/ {
		if(NF>3){
			temp = $2
			for(i=3;i<=NF;i=i+1)
				temp = sprintf("%s=%s",temp,$i)
			gsub("\"","",temp)
			k[trim($1)]=trim(temp)
		}else{
			k[trim($1)]=trim($2)
		}
        next
}
FILENAME!=fp"/setup.properties" && $0 !~ /#|^$/ {
        temp = substr(trim($2),2)
        if(k[temp]!="")
                $2=k[temp]
}
FILENAME!=fp"/setup.properties"{
        if($0 !~ /#|^$/)
                printf("%s=%s\n",$1,$2) >> FILENAME".properties"
        else
                printf("%s\n",$0) >> FILENAME".properties"
}
