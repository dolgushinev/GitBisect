git clone https://github.com/python/cpython.git
cd cpython
#git rev-list --ancestry-path 7f777ed95a19224294949e1b4ce56bbffcb1fe9f..dd104400dc551dd4098f35e12072e12c45822adc > ../commits.txt
git rev-list --ancestry-path $1 > ../commits.txt