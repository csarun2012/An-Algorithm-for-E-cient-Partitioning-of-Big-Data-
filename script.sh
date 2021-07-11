wc -l dataset | awk '{print $1}' > input
count=`cat input`
echo "P " | tr -d '\n' > partition
seq -s ' ' 1 $count >> partition
sed '1q;d' dataset | wc -w | awk '{print $1}' >> input
java Main
chmod +x output
./output
