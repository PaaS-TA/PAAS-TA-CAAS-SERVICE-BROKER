create tablespace ${serviceInstanceId}
datafile '${datPath}${serviceInstanceId}.dat' size ${plan.size} autoextend off;

CREATE TEMPORARY TABLESPACE ${serviceInstanceId}_temp 
tempfile '${datPath}${serviceInstanceId}_temp.dat' 
size ${plan.size} autoextend on next 32m  maxsize ${plan.size} extent management local