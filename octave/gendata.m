function vector = genVector( vSize, number )
    for v = 1:vSize
    vector(v) = number
end

endfunction

function matrix = genMatrix ( mSize, lambda, mi )
    matrix = zeros( mSize , mSize )
    for line = 1:mSize-1
        matrix(line,line) = lambda
        matrix(line,line+1) = -mi
    end
    for col = 1:mSize
        matrix(mSize,col)=1
    end
endfunction

function result = rodada (mSize,lambda,mi)
    matrix = genMatrix(mSize,lambda,mi)
    b = genVector(mSize,0)
    b(mSize)=1
    result = dot(matrix\(b'),0:(mSize-1))
endfunction


function result = runs( nRuns )
    index=1
    result=[]
    for val = 0.05:0.05:0.91
        result(index)= rodada(10,val,1)
    end
endfunction